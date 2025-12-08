(ns day-7.core
  (:require [clojure.math :as math]))

(defn parse-tachyon-manifold
  [file-lines]
  (let [lines (mapv identity file-lines)]
    (loop [beam-start nil
           splitters []
           x 0
           y 0]
      (cond
        (> y (count lines))
        {:dimensions [(count lines) (count (first lines))]
         :beam-origins (set [beam-start])
         :splitters splitters
         :manifold lines}

        (> x (count (first lines)))
        (recur beam-start splitters 0 (inc y))

        (= (get-in lines [y x]) \S)
        (recur [y x] splitters (inc x) y)

        (= (get-in lines [y x]) \^)
        (recur beam-start (conj splitters [y x]) (inc x) y)

        :else
        (recur beam-start splitters (inc x) y)))))

(defn in-bounds?
  [dimensions point]
  (and (>= (first point) 0)
       (>= (second point) 0)
       (< (first point) (first dimensions))
       (< (second point) (second dimensions))))

(defn split-beam
  "Attempts to split the specified beam. Returns nil if unable to split."
  [dimensions manifold beam-origin]
  (loop [y (first beam-origin)]
    (cond
      (> y (first dimensions))
      nil

      (= (get-in manifold [y (second beam-origin)]) \^)
      (filterv (partial in-bounds? dimensions)
               [[y (dec (second beam-origin))] [y (inc (second beam-origin))]])

      :else
      (recur (inc y)))))

(defn split-beams
  "Attempts to split all existing beams."
  [tachyon-manifold]
  (set (reduce into (filterv identity
                             (mapv (partial split-beam (:dimensions tachyon-manifold) (:manifold tachyon-manifold))
                                   (:beam-origins tachyon-manifold))))))

(defn splitter-between?
  [manifold point-a point-b]
  (loop [y (min (first point-a) (first point-b))]
    (cond
      (>= y (max (first point-a) (first point-b)))
      false

      (= (get-in manifold [y (second point-a)]) \^)
      true

      :else
      (recur (inc y)))))

(defn redundant-beam?
  [manifold beams beam]
  (not-empty (filterv #(and (= (second %) (second beam))
                            (< (first %) (first beam))
                            (not (splitter-between? manifold beam %)))
                      beams)))

(defn non-redundant-origins
  "Removes beam origins that are redundant from the manifold."
  [tachyon-manifold]
  (filterv (complement (partial redundant-beam? (:manifold tachyon-manifold) (:beam-origins tachyon-manifold)))
           (:beam-origins tachyon-manifold)))

(defn get-beam-origins
  [tachyon-manifold]
  (loop [tachyon-manifold tachyon-manifold]
    (let [new-origins (into (split-beams tachyon-manifold) (:beam-origins tachyon-manifold))]
      (if (= new-origins (:beam-origins tachyon-manifold))
        (non-redundant-origins tachyon-manifold)
        (recur (assoc tachyon-manifold :beam-origins new-origins))))))

(defn adjacent-to-any?
  [points point]
  (not-empty (filterv #(and (= (first %) (first point))
                            (= 1 (abs (- (second %) (second point)))))
                      points)))

(defn solve-part-1
  [tachyon-manifold]
  (let [beam-origins (get-beam-origins tachyon-manifold)]
    (count (filterv (partial adjacent-to-any? beam-origins) (:splitters tachyon-manifold)))))

(defn debug-solution
  [manifold dimensions points]
  (loop [x 0
         y 0]
    (cond
      (>= y (first dimensions))
      nil

      (>= x (second dimensions))
      (do
        (print "\n")
        (recur 0 (inc y)))

      (not-empty (clojure.set/intersection points (set [[y x]])))
      (do
        (print "|")
        (recur (inc x) y))

      :else
      (do
        (print (get-in manifold [y x]))
        (recur (inc x) y)))))

(comment
  (def olegs-manifold
    {:dimensions   [16 15],
     :beam-origins #{[12 12] [12 6] [10 5] [8 4] [6 7] [10 9] [8 6] [12 2] [14 13] [8 10] [6 5] [4 6] [4 8] [10 11] [14 1] [2 7] [14 5] [6 9] [14 3] [14 7] [10 3]}
     :splitters [[2 7] [4 6] [4 8] [6 5] [6 7] [6 9] [8 4] [8 6] [8 10] [10 3] [10 5] [10 9] [10 11] [12 2] [12 6] [12 12] [14 1] [14 3] [14 5] [14 7] [14 9] [14 13]],,
     :manifold     [".......S......."
                    "..............."
                    ".......^......."
                    "..............."
                    "......^.^......"
                    "..............."
                    ".....^.^.^....."
                    "..............."
                    "....^.^...^...."
                    "..............."
                    "...^.^...^.^..."
                    "..............."
                    "..^...^.....^.."
                    "..............."
                    ".^.^.^.^.^...^."
                    "..............."]})
  (debug-solution (:manifold olegs-manifold)
                  (:dimensions olegs-manifold)
                  (:beam-origins olegs-manifold))

  (def beam-starts
    {:dimensions [16 15]
     :beam-origins #{[8 7] [8 11] [8 9] [2 8] [12 13] [8 3] [14 6] [12 5] [6 6] [12 1] [4 7] [4 9] [10 2] [12 11] [6 4] [8 5] [0 7] [6 8] [10 12] [10 6] [4 5] [10 4] [10 10] [12 3] [14 14] [6 10] [14 2] [2 6] [14 12] [14 0]}
     :splitters [[2 7] [4 6] [4 8] [6 5] [6 7] [6 9] [8 4] [8 6] [8 10] [10 3] [10 5] [10 9] [10 11] [12 2] [12 6] [12 12] [14 1] [14 3] [14 5] [14 7] [14 9] [14 13]]
     :manifold     [".......S......."
                    "..............."
                    ".......^......."
                    "..............."
                    "......^.^......"
                    "..............."
                    ".....^.^.^....."
                    "..............."
                    "....^.^...^...."
                    "..............."
                    "...^.^...^.^..."
                    "..............."
                    "..^...^.....^.."
                    "..............."
                    ".^.^.^.^.^...^."
                    "..............."]})
  (debug-solution (:manifold beam-starts)
                  (:dimensions beam-starts)
                  (:beam-origins beam-starts))

  (def manifold
    {:dimensions   [16 15],
     :beam-origins #{[12 12] [12 6] [10 5] [8 4] [6 7] [10 9] [8 6] [12 2] [14 13] [8 10] [6 5] [4 6] [4 8] [10 11] [14 1] [2 7] [14 5] [6 9] [14 3] [14 7] [10 3]}
     :splitters [[2 7] [4 6] [4 8] [6 5] [6 7] [6 9] [8 4] [8 6] [8 10] [10 3] [10 5] [10 9] [10 11] [12 2] [12 6] [12 12] [14 1] [14 3] [14 5] [14 7] [14 9] [14 13]],
     :manifold     [".......S......."
                    "..............."
                    ".......^......."
                    "..............."
                    "......^.^......"
                    "..............."
                    ".....^.^.^....."
                    "..............."
                    "....^.^...^...."
                    "..............."
                    "...^.^...^.^..."
                    "..............."
                    "..^...^.....^.."
                    "..............."
                    ".^.^.^.^.^...^."
                    "..............."]})
  (debug-solution (:manifold manifold)
                  (:dimensions manifold)
                  (:beam-origins manifold))

  (splitter-between? (:manifold manifold) [6 8] [14 8])

  (non-redundant-origins manifold)

  ,)

(defn solve-part-2
  []
  ())
