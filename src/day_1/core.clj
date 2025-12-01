(ns day-1.core
  (:require [clojure.math :as math]))

(defn parse-move-sequence
  [file-stream]
  (mapv (fn [line]
         [(if (= (nth line 0) \L) :left :right)
          (Integer/parseInt (subs line 1))])
        file-stream))

(defn solve-part-1
  ([move-sequence]
   (solve-part-1 0 50 move-sequence))
  ([zero-count current move-sequence]
   (if (empty? move-sequence)
     zero-count
     (let [[direction quantity] (first move-sequence)
           next (mod ((if (= direction :left) - +)
                      current quantity)
                     100)]
       (recur (if (= next 0) (inc zero-count) zero-count) next (rest move-sequence))))))

(defn solve-part-2
  ([move-sequence]
   (solve-part-2 0 50 move-sequence))
  ([zero-count current move-sequence]
   (if (empty? move-sequence)
     (int zero-count)
     (let [[direction quantity] (first move-sequence)
           next (mod ((if (= direction :left) - +)
                      current quantity)
                     100)

           extra-rotation (max 0
                               (if (= direction :left)
                                 (- quantity current)
                                 (- quantity (- 100 current))))


           new-count (cond-> (quot extra-rotation 100)

                             (and (= direction :left)
                                  (not= current 0)
                                  (= (- quantity extra-rotation) current))
                             inc

                             (and (= direction :right)
                                  (= (- quantity extra-rotation) (- 100 current)))
                             inc)]
       (recur (+ zero-count new-count) next (rest move-sequence))))))