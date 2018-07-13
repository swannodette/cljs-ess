(ns async-fun.core
  (:require [cljs.core.async :refer [put! take! chan go >! <!]]))

(def my-chan (chan))

(defn start-process1 [c]
  (go
    (println "Got a value:" (<! c))
    (println "Got another value:" (<! c))
    (println "And another value:" (<! c))
    (println "Enough is enough!")))

(defn start-process2 [c]
  (go
    (println "Have a number:")
    (>! c 1)
    (println "Have a string:")
    (>! c "cool!")
    (println "Have a vector:")
    (>! c [:foo :bar :baz])
    (println "I'm all out of values!")))
