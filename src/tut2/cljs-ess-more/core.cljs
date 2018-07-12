(ns cljs-ess-more.core
  (:require [goog.dom :as gdom] 
            [reagent.core :as r]))
            
(def app-state 
  (r/atom {:counter 0
           :text ""}))

(defn subview []
  [:div.subview "I'm a subview"])

(defn toggle-view [app-state]
  (let [toggle-state (r/atom false)]
    (fn [_]
      [:div
        [:div
          [:div (str "I've counded " @app-state " clicks!")]
          [:button 
            {:on-click (fn [_] (swap! update app-state :counter inc))}
            "Click Me!"]]
        [:div
          [:label "Toggle Me!"]
          [:checkbox 
            {:checked (:enabled @app-state)
             :on-click (fn [_] (swap! toggle-state complement))}]]])))

(r/render [toggle-view app-state] (gdom/getElement "app"))
