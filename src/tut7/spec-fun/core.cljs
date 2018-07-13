(ns spec-fun.core
    (:require [clojure.test.check :as tc]
              [clojure.test.check.generators]
              [clojure.spec.alpha :as s]
              [clojure.spec.test.alpha :as st]
              [clojure.pprint :refer [pprint]]))

  ;; basics

(s/def ::completed boolean?)

(s/def ::title
  (s/with-gen
    string?
    #(s/gen
      #{"Walk dog"
        "Buy OJ"
        "Buy milk"
        "Answer emails"
        "Write ReactiveConf talk"})))

(s/def ::timestamp inst?)

(s/def ::task
  (s/keys :req [::title ::completed]
          :opt [::timestamp]))

(comment
  (s/valid? ::task
    {::title "foo" ::completed 1})

  (s/valid? ::task
    {::title "foo" ::completed true})

  (s/explain ::task
    {::title "foo" ::completed 1}))


(s/def ::todos (s/* ::task))

(s/def ::at-least-one-todo (s/+ ::task))

(comment
  (s/valid? ::todos
    [{::title "foo" ::completed true} "bar"])

  (s/explain ::todos
    [{::title "foo" ::completed true} "bar"])

  (s/valid? ::todos
    [{::title "foo" ::completed true}
     {::title "bar" ::completed false}]))


(comment
  (pprint (s/exercise ::todos 6))
  (pprint
    (-> (s/exercise ::todos 6) last first)))


(s/def ::filter #{:all :active :completed})

(s/def ::app-state
  (s/keys :req [::filter ::todos]))

(comment
  (pprint
    (-> (s/exercise ::app-state 6) last first)))


(s/def ::valid-app-state
  (s/or
    :view-completed
    (s/and ::app-state
           #(= :completed (::filter %))
           #(every? ::completed (::todos %)))

    :view-active
    (s/and ::app-state
           #(= :active (::filter %))
           #(every? (comp not ::completed) (::todos %)))

    :view-all
    (s/and ::app-state
           #(= :all (::filter %)))))

(comment
  (def dummy-app-state
    {::filter :completed
     ::todos [{::title "Walk dog"
               ::completed true
               ::num-or-str :foo}
              {::title "Buy OJ"
               ::completed true}]})

  (s/valid? ::valid-app-state dummy-app-state)
  (s/explain ::valid-app-state dummy-app-state)

  (s/assert ::valid-app-state dummy-app-state)

  (second
    (s/conform ::valid-app-state dummy-app-state)))


(s/fdef vienna-js.core/apply-filter
  :args (s/cat :app-state ::app-state
               :todo-filter ::filter)
  :ret ::valid-app-state)

(defn apply-filter [app-state todo-filter]
  (-> app-state
    (assoc ::filter todo-filter)
    (update-in [::todos]
      (fn [todos]
        (case todo-filter
          :completed (into [] (filter ::completed) todos)
          :active (into [] (remove ::completed) todos)
          todos)))))

(comment
  (pprint
    (apply-filter
      {::filter :all
       ::todos [{::title "Walk dog"
                 ::completed true}
                {::title "Buy OJ"
                 ::completed false}]}
      :active))

  (pprint
    (-> (st/check `apply-filter
          {::tc/opts {:num-tests 100}})
      first :clojure.test.check/ret
      :shrunk :smallest))

  (apply apply-filter
    '({:vienna-js.core/filter :completed,
       :vienna-js.core/todos
       ({:vienna-js.core/title "Walk dog",
         :vienna-js.core/completed true})})
    :active))
