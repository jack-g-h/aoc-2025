(ns core
  (:require [clojure.java.io :as io]
            [day-1.core :as day-1]
            [day-2.core :as day-2]
            [day-3.core :as day-3]))

(defn day-parser
  [day day-parse-fn]
  #(with-open [input-file (io/reader (str "resources/" day "/" %))]
     (day-parse-fn (line-seq input-file))))

;;; Run Day 1
(comment
  (def parser (day-parser "day-1" day-1/parse-move-sequence))
  (parser "example")

  (day-1/solve-part-1 (parser "example"))
  (day-1/solve-part-1 (parser "actual"))

  (day-1/solve-part-2 (parser "example"))
  (day-1/solve-part-2 (parser "actual"))
  ,)

;;; Run Day 2
(comment
  (def parser (day-parser "day-2" day-2/parse-ranges))
  (parser "example")

  (mapv day-2/invalid-number? (reduce into (parser "example")))

  (day-2/solve-part-1 (parser "example"))
  (day-2/solve-part-1 (parser "actual"))

  (day-2/solve-part-2 (parser "example"))
  (day-2/solve-part-2 (parser "actual"))
  ,)

;;; Run Day 3
(comment
  (def parser (day-parser "day-3" day-3/parse-batteries))
  (parser "example")

  (day-3/solve-part-1 (parser "example"))
  (day-3/solve-part-1 (parser "actual"))

  (day-3/solve-part-2 (parser "example"))
  (day-3/solve-part-2 (parser "actual"))
  ,)
