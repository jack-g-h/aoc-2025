(ns core
  (:require [clojure.java.io :as io]
            [day-1.core :as day-1]
            [day-2.core :as day-2]
            [day-3.core :as day-3]
            [day-4.core :as day-4]
            [day-5.core :as day-5]))

(defn day-parser
  [day day-parse-fn]
  #(with-open [input-file (io/reader (str "resources/" day "/" %))]
     (day-parse-fn (line-seq input-file))))

;;; Run Day 1
(comment
  (def parser-1 (day-parser "day-1" day-1/parse-move-sequence))
  (parser-1 "example")

  (day-1/solve-part-1 (parser-1 "example"))
  (day-1/solve-part-1 (parser-1 "actual"))

  (day-1/solve-part-2 (parser-1 "example"))
  (day-1/solve-part-2 (parser-1 "actual"))
  ,)

;;; Run Day 2
(comment
  (def parser-2 (day-parser "day-2" day-2/parse-ranges))
  (parser-2 "example")

  (day-2/solve-part-1 (parser-2 "example"))
  (day-2/solve-part-1 (parser-2 "actual"))

  (day-2/solve-part-2 (parser-2 "example"))
  (day-2/solve-part-2 (parser-2 "actual"))
  ,)

;;; Run Day 3
(comment
  (def parser-3 (day-parser "day-3" day-3/parse-batteries))
  (parser-3 "example")

  (day-3/solve-part-1 (parser-3 "example"))
  (day-3/solve-part-1 (parser-3 "actual"))

  (day-3/solve-part-2 (parser-3 "example"))
  (day-3/solve-part-2 (parser-3 "actual"))
  ,)

;;; Run Day 4
(comment
  (def parser-4 (day-parser "day-4" day-4/parse-rolls))
  (parser-4 "example")

  (day-4/solve-part-1 (parser-4 "example"))
  (day-4/solve-part-1 (parser-4 "actual"))

  (day-4/solve-part-2 (parser-4 "example"))
  (day-4/solve-part-2 (parser-4 "actual"))
  ,)

;;; Run Day 5
(comment
  (def parser-5 (day-parser "day-5" day-5/parse-database))
  (parser-5 "example")

  (day-5/solve-part-1 (parser-5 "example"))
  (day-5/solve-part-1 (parser-5 "actual"))

  (day-5/solve-part-2 (parser-5 "example"))
  (day-5/solve-part-2 (parser-5 "actual"))
  ,)
