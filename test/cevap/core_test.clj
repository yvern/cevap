(ns cevap.core-test
  (:require [clojure.test :refer :all]
            [cevap.core :refer :all]
            [clojure.core.async :as a]))

(defn checks [chs v] (mapv #(is (= v (a/<!! %))) chs))

(deftest check
  (testing "some fibonacci values"
    (let [req {:parameters {:query {:max 1000}}}
          chs ((juxt fib-rec fib-mem fib-iter fib-mat) req)
          checkit (partial checks chs)]
        (mapv checkit [1 1 2 3 5 8 13 21 34 55 89 144 233 377 610 987])
        (mapv #(is (nil? (a/poll! %))) chs))))
