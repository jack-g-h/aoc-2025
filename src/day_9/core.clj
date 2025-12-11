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

(defn categorize-line
  [line]
  (if (= (first (first line)) (first (second line)))
    {:direction 'vertical :level (first (first line)) :bounds (into [] (sort (mapv second line)))}
    {:direction 'horizontal :level (second (first line)) :bounds (into [] (sort (mapv first line)))}))

(defn vertices->lines
  "Takes the in-order vertices of a n-gon and returns the n-gon's vertices."
  [vertices]
  (mapv categorize-line
        (mapv (partial conj [])
              vertices
              (conj (into [] (rest vertices)) (first vertices)))))

(defn lines-intersect?
  [rectangle-line bound-line]
  (if (= (:direction rectangle-line) (:direction bound-line))
    false
    (condp = (:side rectangle-line)
      'top (and (<= (first (:bounds bound-line)) (:level rectangle-line))
                (> (second (:bounds bound-line)) (:level rectangle-line))
                (> (:level bound-line) (first (:bounds rectangle-line)))
                (< (:level bound-line) (second (:bounds rectangle-line))))
      'bottom (and (< (first (:bounds bound-line)) (:level rectangle-line))
                   (>= (second (:bounds bound-line)) (:level rectangle-line))
                   (> (:level bound-line) (first (:bounds rectangle-line)))
                   (< (:level bound-line) (second (:bounds rectangle-line))))
      'left (and (<= (first (:bounds bound-line)) (:level rectangle-line))
                 (> (second (:bounds bound-line)) (:level rectangle-line))
                 (> (:level bound-line) (first (:bounds rectangle-line)))
                 (< (:level bound-line) (second (:bounds rectangle-line))))
      'right (and (< (first (:bounds bound-line)) (:level rectangle-line))
                  (>= (second (:bounds bound-line)) (:level rectangle-line))
                  (> (:level bound-line) (first (:bounds rectangle-line)))
                  (< (:level bound-line) (second (:bounds rectangle-line)))))))

(defn rectangle-lines
  [point-a point-b]
  (let [top-line (assoc (categorize-line [[(min (first point-a) (first point-b)) (min (second point-a) (second point-b))]
                                          [(max (first point-a) (first point-b)) (min (second point-a) (second point-b))]])
                   :side 'top)
        bottom-line (assoc (categorize-line [[(min (first point-a) (first point-b)) (max (second point-a) (second point-b))]
                                             [(max (first point-a) (first point-b)) (max (second point-a) (second point-b))]])
                      :side 'bottom)

        left-line (assoc (categorize-line [[(min (first point-a) (first point-b)) (min (second point-a) (second point-b))]
                                           [(min (first point-a) (first point-b)) (max (second point-a) (second point-b))]])
                    :side 'left)
        right-line (assoc (categorize-line [[(max (first point-a) (first point-b)) (min (second point-a) (second point-b))]
                                            [(max (first point-a) (first point-b)) (max (second point-a) (second point-b))]])
                     :side 'right)]
    [top-line bottom-line left-line right-line]))

(defn valid-rectangle?
  [bounds point-a point-b]
  (let [lines (rectangle-lines point-a point-b)]
    (every? #(not-any? (partial lines-intersect? %) bounds) lines)))

(defn solve-part-2
  [red-tiles]
  (let [bounds (vertices->lines red-tiles)
        potential-rectangles (reduce into
                                     (mapv (fn [first-corner]
                                             (mapv (partial conj [first-corner])
                                                   red-tiles))
                                           red-tiles))
        valid-rectangles (filterv (partial apply valid-rectangle? bounds) potential-rectangles)]
    (apply rectangle-area (first (sort-by (partial apply rectangle-area) > valid-rectangles)))))