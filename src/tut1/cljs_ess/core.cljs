(ns cljs-ess.core
  (:require [goog.dom :as gdom]
            [reagent.core :as r]))

(def state (r/atom 0))

(defn counter [text]
  [:div
    [:p (str "Hello Reagent! I've counted " @state " clicks so far")]
    [:button 
      {:on-click (fn [e] (swap! state inc))} 
      "Click Me!"]])

(r/render [counter state] (gdom/getElement "app"))

