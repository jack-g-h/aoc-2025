(ns day-4.core
  (:require [clojure.math :as math]))

(defn char->item
  [char]
  ({\@ :roll
    \. :empty}
   char))

(defn parse-rolls
  [file-lines]
  (mapv (fn [line]
          (mapv char->item line))
        file-lines))

(defn adjacent-rolls
  [roll-layout pos-x pos-y]
  (loop [x (max (dec pos-x) 0)
         y (max (dec pos-y) 0)
         adjacent 0]
    (cond
      (and (= x pos-x)
           (= y pos-y))
      (recur (inc x) y adjacent)

      (or (> y (count roll-layout))
          (> y (inc pos-y)))
      adjacent

      (or (> x (count (first roll-layout)))
          (> x (inc pos-x)))
      (recur (max (dec pos-x) 0) (inc y) adjacent)

      :else
      (recur (inc x)
             y
             (if (= (get-in roll-layout [y x]) :roll)
               (inc adjacent)
               adjacent)))))

(defn solve-part-1
  [roll-layout]
  (loop [x 0
         y 0
         accessible 0]
    (cond
      (> y (count roll-layout))
      accessible

      (> x (count (first roll-layout)))
      (recur 0 (inc y) accessible)

      :else
      (recur (inc x)
             y
             (if (and (= (get-in roll-layout [y x]) :roll)
                      (< (adjacent-rolls roll-layout x y) 4))
               (inc accessible)
               accessible)))))

(defn roll-matrix->roll-positions
  [roll-layout]
  (loop [x 0
         y 0
         positions []]
    (cond
      (> y (count roll-layout))
      positions

      (> x (count (first roll-layout)))
      (recur 0 (inc y) positions)

      :else
      (recur (inc x)
             y
             (if (= (get-in roll-layout [y x]) :roll)
               (conj positions [y x])
               positions)))))

(defn position-distance
  [position-a position-b]
  (math/sqrt (+ (math/pow (- (first position-a) (first position-b)) 2)
                (math/pow (- (second position-a) (second position-b)) 2))))

(defn removable?
  [all-positions position]
  (< (count (filter #(and (not= position %)
                          ((comp (partial > 2)
                                 (partial position-distance position))
                           %))
                    all-positions))
      4))

(defn solve-part-2
  [roll-layout]
  (let [roll-positions (roll-matrix->roll-positions roll-layout)]
    (loop [positions roll-positions
           trimmed (filterv (complement (partial removable? positions)) positions)]
      (println (count positions))
      (if (= (count positions) (count trimmed))
        (- (count roll-positions) (count trimmed))
        (let [next-trimming (filterv (complement (partial removable? trimmed)) trimmed)]
          (recur trimmed
                 next-trimming))))))
