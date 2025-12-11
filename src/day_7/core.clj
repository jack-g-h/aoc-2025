(ns day-7.core
  (:require
    [clojure.set :as set]))

(defn parse-tachyon-manifold
  [file-lines]
  (let [lines (mapv identity file-lines)]
    (loop [beam-start nil
           x 0
           y 0]
      (cond
        (> y (count lines))
        {:beam-origin beam-start
         :manifold lines}

        (> x (count (first lines)))
        (recur beam-start 0 (inc y))

        (= (get-in lines [y x]) \S)
        (recur [y x] (inc x) y)

        :else
        (recur beam-start (inc x) y)))))

(defn move-splitless
  [tachyon-manifold layer beam-locations]
  (loop [lowered-beams #{}
         to-consider beam-locations]
    (cond
      (empty? to-consider)
      lowered-beams

      (= \^ (get-in tachyon-manifold [(inc layer) (first to-consider)]))
      (recur lowered-beams (rest to-consider))

      :else
      (recur (conj lowered-beams (first to-consider)) (rest to-consider)))))

(defn move-splitters
  [beam-locations splitless]
  (loop [beams splitless
         to-consider (set/difference beam-locations splitless)]
    (cond
      (empty? to-consider)
      beams

      :else
      (let [with-splits (conj beams (dec (first to-consider)) (inc (first to-consider)))]
        (if (> (count with-splits) (count beams))
          (recur with-splits (rest to-consider))
          (recur with-splits (rest to-consider)))))))

(defn move-downwards
  [tachyon-manifold layer beam-locations]
  (let [splitless (move-splitless tachyon-manifold layer beam-locations)]
    {:beams (move-splitters beam-locations splitless)
     :new-splits (count (set/difference beam-locations splitless))}))
(def move-downwards (memoize move-downwards))

(defn solve-part-1
  [tachyon-manifold]
  (loop [y (first (:beam-origin tachyon-manifold))
         beams #{(second (:beam-origin tachyon-manifold))}
         splits 0]
    (if (>= (inc y) (count (:manifold tachyon-manifold)))
      splits
      (let [after-move (move-downwards (:manifold tachyon-manifold) y beams)]
        (recur (inc y) (:beams after-move) (+ splits (:new-splits after-move)))))))

(defn count-timelines
  [tachyon-manifold beam-y beam-x]
  (if (>= (inc beam-y) (count tachyon-manifold))
    1
    (loop [beam-y beam-y]
      (let [next-beam (:beams (move-downwards tachyon-manifold beam-y #{beam-x}))]
        (cond
          (>= beam-y (count tachyon-manifold)) 1
          (= 1 (count next-beam)) (recur (inc beam-y))
          :else (reduce + (mapv (partial count-timelines tachyon-manifold (inc beam-y))
                                next-beam)))))))
(def count-timelines (memoize count-timelines))

(defn solve-part-2
  [tachyon-manifold]
  (count-timelines (:manifold tachyon-manifold) (first (:beam-origin tachyon-manifold)) (second (:beam-origin tachyon-manifold))))