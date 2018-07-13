(ns test-fun.core
  (:require [cljs.test :as test :refer [deftest is run-tests]]))

(deftest test-add
  (is (= (+ 1 1) 2)))

(deftest test-conj
  (is (= (conj [1 2] 3)
         [1 2 3])))

(defn -main [& args]
  (run-tests))