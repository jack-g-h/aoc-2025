(ns day-8.core
  (:require
    [clojure.string :as string]
    [clojure.set :as set]
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

(defn box-distances
  [all-boxes target-box]
  (zipmap (mapv (partial euclid-distance target-box) all-boxes) all-boxes))

(defn all-box-distances
  [boxes]
  (->> boxes
       (mapv (fn [box]
               (update-vals (box-distances boxes box) (comp (partial conj #{})
                                                            (partial conj #{box})))))
       (apply merge-with into)
       (into (sorted-map-by <))))

(defn join-sets-on
  [sets join-items]
  (let [untouched (filterv (comp empty? (partial set/intersection join-items))
                           sets)
        to-join (filterv (comp not empty? (partial set/intersection join-items))
                         sets)]
    (conj untouched (apply set/union to-join))))

(defn perform-n-links
  [n boxes box-distances]
  (loop [remaining-distances (rest (keys box-distances))
         remaining n
         circuits (mapv (partial conj #{}) boxes)]
    (cond
      (<= remaining 0) circuits
      :else
      (let [links (get box-distances (first remaining-distances))
            linked-circuits (reduce join-sets-on circuits (take remaining links))]
        (recur (rest remaining-distances) (- remaining (count links)) linked-circuits)))))

(defn solve-part-1
  [junction-boxes n]
  (->> (all-box-distances junction-boxes)
       (perform-n-links n junction-boxes)
       (sort-by count >)
       (take 3)
       (map count)
       (reduce *)))

(defn last-connection
  "This function abuses the fact that for no distance > 0 is there more than
   one pair of junction boxes (for my input).

   This might be an assumption of the problem as tie conditions aren't
   detailed, but it is never explicitly stated."
  [boxes box-distances]
  (loop [remaining-distances (rest (keys box-distances))
         circuits (mapv (partial conj #{}) boxes)]
    (let [links (get box-distances (first remaining-distances))
          linked-circuits (reduce join-sets-on circuits links)]
      (if (= 1 (count linked-circuits))
        links
        (recur (rest remaining-distances) linked-circuits)))))

(defn solve-part-2
  [junction-boxes]
  (->> (all-box-distances junction-boxes)
       (last-connection junction-boxes)
       first
       (mapv first)
       (reduce *)))
