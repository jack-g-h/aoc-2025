(ns day-3.core
  (:require
    [clojure.string :as string]
    [clojure.math :as math]))

(defn parse-batteries
  [file-lines]
  (mapv (fn [line]
          (mapv #(Integer/parseInt (str %)) line))
        file-lines))

(def largest-n-joltage)
(defn largest-n-joltage-fn
  [n batteries]
  (if (or (= 0 n)
          (empty? batteries))
    0
    (apply max
           (mapv #(let [prior-digits (largest-n-joltage (dec n)
                                                        (into [] (take % batteries)))]
                    (+ (nth batteries %) (* 10 prior-digits)))
                 (range (count batteries))))))
(def largest-n-joltage (memoize largest-n-joltage-fn))

(defn solve-part-1
  [banks]
  (reduce + (mapv (partial largest-n-joltage 2) banks)))

(defn solve-part-2
  [banks]
  (reduce + (mapv (partial largest-n-joltage 12) banks)))