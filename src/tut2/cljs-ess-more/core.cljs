(ns cljs-ess-more.core
  (:require [goog.dom :as gdom] 
            [reagent.core :as r]))
            
(def app-state 
  (r/atom {:counter 0}))

(defn sub-subview []
  [:div.subview "I'm a subview"])

(defn toggle-view [app-state]
  (let [toggle-state (r/atom false)]
    (fn [_]
      [:div
        [:div.section
          [:div (str "I've counted " (:counter @app-state) " clicks!")]
          [:button 
            {:on-click (fn [_] (swap! app-state update :counter inc))}
            "Click Me!"]]
        [:div.section
          [:label "Toggle Me!"
            [:input
              {:type "checkbox"
               :checked @toggle-state
               :on-change (fn [_] (swap! toggle-state not))}]]
          (when @toggle-state
            [sub-subview])]])))

(r/render [toggle-view app-state] (gdom/getElement "app"))
