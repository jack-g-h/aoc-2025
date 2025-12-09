(ns day-9.core
  (:require [clojure.string :as string]))

(defn parse-tiles
  [file-lines]
  (mapv (fn [line]
          (mapv #(Long/parseLong %)
                (string/split line #",")))
        file-lines))

(defn rectangle-area
  [corner-a corner-b]
  (* (inc (abs (- (first corner-a) (first corner-b))))
     (inc (abs (- (second corner-a) (second corner-b))))))

(defn solve-part-1
  [red-tiles]
  (apply max (reduce into (mapv (fn [first-corner]
                                  (mapv (partial rectangle-area first-corner) red-tiles))
                                red-tiles))))

(defn all-colored-rectangle?
  [red-tiles corner-a corner-b])

(defn solve-part-2
  [red-tiles]
  )