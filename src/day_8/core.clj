(ns day-8.core
  (:require
    [clojure.string :as string]
    [clojure.math :as math]))

(defn parse-junction-boxes
  [file-lines]
  (mapv (fn [line]
          (mapv #(Long/parseLong %)
                (string/split line #",")))
        file-lines))

(defn euclid-distance
  [point-a point-b]
  (math/sqrt (reduce + (mapv (comp #(math/pow % 2) -) point-a point-b))))
(def euclid-distance (memoize euclid-distance))

(defn closest-box
  [junction-boxes target-box]
  (let [box-distances (mapv (fn [box]
                              [(euclid-distance box target-box) box])
                            junction-boxes)
        sorted-distances (sort #(compare (first %1) (first %2))
                               box-distances)]
    (if (and (zero? (first (first sorted-distances))) (second sorted-distances))
      (second sorted-distances)
      (first sorted-distances))))

(defn circuit-distance
  [circuit-a circuit-b]
  (if (= circuit-a circuit-b)
    ##Inf
    (first (first (mapv (partial closest-box circuit-a) circuit-b)))))
(def circuit-distance (memoize circuit-distance))

(defn closest-circuit
  [circuits target-circuit]
  (let [circuit-distances (mapv (fn [circuit]
                                  (future [(circuit-distance circuit target-circuit) circuit]))
                                circuits)
        sorted-distances (sort #(compare (first %1) (first %2))
                               (mapv deref circuit-distances))]
    (if (= 0.0 (first (first sorted-distances)))
      (second sorted-distances)
      (first sorted-distances))))

(defn closest-circuits
  [circuits]
  (loop [distance nil
         closest nil
         remaining circuits]
    (let [closest-to-first (closest-circuit circuits (first remaining))]
      (cond
        (empty? remaining)
        closest

        (or (not distance) (< (first closest-to-first) distance))
        (recur (first closest-to-first) [(first remaining) (second closest-to-first)] (rest remaining))

        :else
        (recur distance closest (rest remaining))))))

(defn make-connection
  [circuits]
  (let [to-merge (closest-circuits circuits)
        others (filterv #(and (not= % (first to-merge))
                              (not= % (second to-merge)))
                        circuits)]
    (conj others (apply into to-merge))))

(defn make-n-connections
  [circuits n]
  (loop [circuits circuits
         count 0]
    (if (zero? (mod count 100))
      (println count))
    (if (>= count n)
      circuits
      (recur (make-connection circuits) (inc count)))))

(defn solve-part-1
  [junction-boxes n]
  (let [circuits (make-n-connections (mapv (partial conj (set [])) junction-boxes) (dec n))
        by-size (reverse (sort-by count circuits))]
    (reduce * (mapv count (take 3 by-size)))))

(defn solve-part-2
  []
  ())
