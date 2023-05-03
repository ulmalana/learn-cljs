(ns learn-cljs.ch29-notification-test
    (:require
     [cljs.test :refer-macros [deftest is testing]]
     [learn-cljs.ch29-notification :refer [multiply]]))

(deftest multiply-test
  (is (= (* 1 2) (multiply 1 2))))

(deftest multiply-test-2
  (is (= (* 75 10) (multiply 10 75))))
