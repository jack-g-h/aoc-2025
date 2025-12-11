(ns day-11.core
  (:require
    [clojure.string :as string]))

(defn parse-devices
  [file-lines]
  (reduce into (mapv (fn [line]
                       (let [devices (string/split line #" ")
                             source-device (subs (first devices) 0 (dec (count (first devices))))]
                         {source-device (rest devices)}))
                     file-lines)))

(def paths-to)
(defn paths-to
  [destination devices current]
  (cond
    (= current destination) 1
    (empty? devices) 0
    (not (get devices current)) 0
    :else (reduce +
                  (mapv (partial paths-to destination devices)
                        (get devices current)))))
(def paths-to (memoize paths-to))

(defn solve-part-1
  [devices]
  (paths-to "out" devices "you"))

(defn count-paths-with
  [devices required destination current]
  (cond
    (and (= current destination) (every? identity (vals required))) 1
    (= current destination) 0
    :else
    (let [updated-required (if (contains? required current)
                             (assoc required current true)
                             required)
          path-counts (mapv (partial count-paths-with devices updated-required destination)
                            (get devices current))]
      (reduce + path-counts))))
(def count-paths-with (memoize count-paths-with))

(defn solve-part-2
  [devices]
  (count-paths-with devices {"fft" false "dac" false} "out" "svr"))
