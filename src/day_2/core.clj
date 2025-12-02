(ns day-2.core
  (:require [clojure.string :as string]))

(defn parse-ranges
  [lines]
  (let [line (first lines)
        ranges (string/split line #",")]
    (mapv (fn [range]
            (mapv #(Long/parseLong %) (string/split range #"-"))) ranges)))

(defn ranges->numbers
  [ranges]
  (mapv (fn [[start end]] (range start (inc end))) ranges))

(defn invalid-number?
  [number]
  (let [number-string (str number)
        string-length (count number-string)]
    (and (even? string-length)
         (= (subs number-string 0 (/ string-length 2))
            (subs number-string (/ string-length 2))))))

(defn solve-part-1
  [ranges]
  (let [all-numbers (reduce into (ranges->numbers ranges))]
    (reduce + (filterv invalid-number? all-numbers))))

(defn repeated-string?
  [string prefix]
  (empty? (string/split string (re-pattern prefix))))

(defn invalid-number-original?
  [number]
  (let [number-string (str number)]
    (loop [substring-end 1]
      (if (>= substring-end (count number-string))
        false
        (if (repeated-string? number-string (subs number-string 0 substring-end))
          true
          (recur (inc substring-end)))))))

(defn solve-part-2
  [ranges]
  (let [all-numbers (reduce into (ranges->numbers ranges))]
    (reduce + (filterv invalid-number-original? all-numbers))))