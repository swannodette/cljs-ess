(ns code-split.page-b
  (:require [cljs.loader :as loader]))

(defn woz []
  (println "WOZ!"))

(loader/set-loaded! :page-b)
