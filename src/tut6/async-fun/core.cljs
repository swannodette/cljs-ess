(ns async-fun.core
  (:require [cljs.core.async :refer [put! take! chan go >! <!]]))

(defn start-process [c]
  (go
    (println "Got a value:" (<! c))
    (println "Got another value:" (<! c))
    (println "And another value:" (<! c))
    (println "Enough is enough!")))

