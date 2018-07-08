(ns cljs-ess.core
  (:require [goog.dom :as gdom]
            [reagent.core :as r]))

(r/render [:p "Hello world!"] (gdom/getElement "app"))

