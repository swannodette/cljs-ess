(ns webpack-fun.core
  (:require [goog.dom :as gdom]
            [reagent.core :as r]
            [moment :as m]
            [react]
            [react-datepicker :as rd]))

(defn handle-change [date state]
  (reset! state date))

(defn date-picker [state]
  (react/createElement rd/DatePicker
     #js {:selected @state
          :onChange #(handle-change % state)}))

(defn cljs-date-picker []
  (let [state (r/atom (m/moment))]
    (fn []
      [date-picker state])))

(r/render [cljs-date-picker] (gdom/getElement "app"))
