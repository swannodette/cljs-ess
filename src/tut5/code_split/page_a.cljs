(ns code-split.page-a
  (:require [goog.dom :as gdom]
            [goog.events :as events]
            [cljs.loader :as loader])
  (:import [goog.events EventType]))

(events/listen (gdom/getElement "button") EventType.CLICK
  (fn [e]
    (loader/load :page-b
      (fn []
        ((resolve 'code-split.page-b/woz))))))

(loader/set-loaded! :page-a)
