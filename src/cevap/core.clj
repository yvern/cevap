(ns cevap.core
  (:require [uncomplicate.neanderthal
             [core :refer :all]
             [native :refer :all]]
            [yada.yada :as yada]
            [clojure.core.async :as a]
            [jsonista.core :as j])
  (:gen-class))

(defn fib 
  "your usual recursive, naive fibonacci"
  [n] (if (<= n 2) 
          1
          (+ (fib (dec n)) 
             (fib (dec (dec n))))))

(defn fib-rec 
  "turning the recursive approach into a channel of fibonacci numbers.
   this should be the slowest, but ends up noticeable only for large numbers"
  [req]
  (let [lim (bigint (get-in req [:parameters :query :max]))
        res (a/chan)]
      (a/go-loop [n 1]
        (let [f (fib n)]
          (when (< f lim)
                (a/>! res f)
                (recur (inc n)))))
      res))

(defn fib-mem
  "same the recursive one, but using memoization"
  [req]
  (let [lim (bigint (get-in req [:parameters :query :max]))
        res (a/chan)
        mfib (memoize fib)]
      (a/go-loop [n 1]
        (let [f (mfib n)]
          (when (< f lim)
                (a/>! res f)
                (recur (inc n)))))
      res))

(defn fib-iter 
  "iterative approach, generating a sequence that only depends on the last state"
  [req]
  (let [lim (bigint (get-in req [:parameters :query :max]))
        fib0 [1 1]
        fibs (iterate (fn [[a b]] [b (+ a b)]) fib0)
        fib (map first fibs)
        cap-fib (take-while #(and (< % lim) (not (Double/isNaN %))) fib)]
      (a/to-chan cap-fib)))

(defn fib-mat
  "using matrix exponentiation is the fastest way to generate the sequence"
  [req]
  (let [lim (bigint (get-in req [:parameters :query :max]))
        fib0 (dge 2 2 [1 1 1 0])
        fibs (iterate #(mm % fib0) fib0)
        fib (map #(bigint (entry % 0 1)) fibs)
        cap-fib (take-while #(and (< % lim) (not (Double/isNaN %))) fib)]
      (a/to-chan cap-fib)))

(defn fibr 
  "turns a function into an http handler, ready to be served"
  [resp]
  (yada/handler (yada/resource 
                {:methods {:get {:produces "text/event-stream"
                                 :parameters {:query {:max Double}}
                                 :response resp}}})))

(defn -main
  [& args]
  (yada/listener 
    ["/" {"fib/" {"iter" (fibr fib-iter)
                  "rec" (fibr fib-rec)
                  "mem" (fibr fib-mem)
                  "mat" (fibr fib-mat)}}]
    {:port 3000}))
