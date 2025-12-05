(ns day-5.core
  (:require [clojure.string :as string]))

(defn parse-range
  [range-str]
  (let [ends (string/split range-str #"-")]
    [(Long/parseLong (first ends)) (Long/parseLong (second ends))]))

(defn parse-database
  [file-lines]
  (let [all-lines (mapv identity file-lines)
        ranges (filterv #(string/includes? % "-") all-lines)
        ingredients (filterv #(and (not (string/includes? % "-"))
                                   (not= "" %))
                             all-lines)]
    {:fresh-ranges (mapv parse-range ranges)
     :ingredients  (mapv #(Long/parseLong %) ingredients)}))

(defn in-range?
  [range number]
  (and (>= number (first range))
       (<= number (second range))))

(defn solve-part-1
  [database-info]
  (count (filterv (fn [ingredient]
                    (some #(in-range? % ingredient) (:fresh-ranges database-info)))
                  (:ingredients database-info))))

(defn range-contained?
  [range-a range-b]
  (and (in-range? range-a (first range-b))
       (in-range? range-a (second range-b))))

(defn ranges-overlap?
  [range-a range-b]
  (or (in-range? range-a (first range-b))
      (in-range? range-a (second range-b))
      (in-range? range-b (first range-a))
      (in-range? range-b (second range-a))))

(defn merge-ranges
  "Assumes (ranges-overlap? range-a range-b)."
  [range-a range-b]
  (cond
    (range-contained? range-a range-b) range-a
    (range-contained? range-b range-a) range-b
    (in-range? range-a (first range-b)) [(first range-a) (second range-b)]
    (in-range? range-a (second range-b)) [(first range-b) (second range-a)]
    (in-range? range-b (first range-a)) [(first range-b) (second range-a)]
    (in-range? range-b (second range-a)) [(first range-a) (second range-b)]
    :else nil))

(defn range-size
  [range]
  (inc (- (second range) (first range))))

(defn reduce-range-count
  "Represents the provided ranges in the minimum possible number of ranges."
  [ranges]
  (loop [remaining-ranges ranges
         combined []]
    (cond
      (and (empty? remaining-ranges) (= ranges combined))
      combined

      (empty? remaining-ranges)
      (reduce-range-count combined)

      (not-any? (partial ranges-overlap? (first remaining-ranges)) combined)
      (recur (rest remaining-ranges) (conj combined (first remaining-ranges)))

      :else
      (let [updated-combined (mapv (fn [range]
                                     (if (ranges-overlap? range (first remaining-ranges))
                                       (merge-ranges range (first remaining-ranges))
                                       range))
                                   combined)]
        (recur (rest remaining-ranges) updated-combined)))))

(defn solve-part-2
  [database-info]
  (reduce + (mapv range-size (reduce-range-count (:fresh-ranges database-info)))))

(comment
  (merge-ranges [16 20] [10 18])
  ,)