(ns core
  (:require [clojure.java.io :as io]
            [day-1.core :as day-1]
            [day-2.core :as day-2]
            [day-3.core :as day-3]
            [day-4.core :as day-4]
            [day-5.core :as day-5]
            [day-6.core :as day-6]
            [day-7.core :as day-7]
            [day-8.core :as day-8]
            [day-9.core :as day-9]
            [day-10.core :as day-10]
            [day-11.core :as day-11]
            [clojure.string]))

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

;;; Run Day 6
(comment
  (def parser-6-1 (day-parser "day-6" day-6/parse-human-problems))
  (parser-6-1 "example")

  (day-6/solve-homework (parser-6-1 "example"))
  (day-6/solve-homework (parser-6-1 "actual"))

  (def parser-6-2 (day-parser "day-6" day-6/parse-cephalopod-problems))
  (parser-6-2 "example")

  (day-6/solve-homework (parser-6-2 "example"))
  (day-6/solve-homework (parser-6-2 "actual"))
  ,)

;;; Run Day 7
(comment
  (def parser-7 (day-parser "day-7" day-7/parse-tachyon-manifold))
  (parser-7 "example")

  (day-7/solve-part-1 (parser-7 "example"))
  (day-7/solve-part-1 (parser-7 "actual"))

  (day-7/solve-part-2 (parser-7 "example"))
  (day-7/solve-part-2 (parser-7 "actual"))
  ,)

;;; Run Day 8
(comment
  (def parser-8 (day-parser "day-8" day-8/parse-junction-boxes))
  (parser-8 "example")

  (day-8/solve-part-1 (parser-8 "example") 10)
  (day-8/solve-part-1 (parser-8 "actual") 1000)

  (day-8/solve-part-2 (parser-8 "example"))
  (day-8/solve-part-2 (parser-8 "actual"))
  ,)

;;; Run Day 9
(comment
  (def parser-9 (day-parser "day-9" day-9/parse-tiles))
  (parser-9 "example")

  (day-9/solve-part-1 (parser-9 "example"))
  (day-9/solve-part-1 (parser-9 "actual"))

  (day-9/solve-part-2 (parser-9 "example"))
  (day-9/solve-part-2 (parser-9 "actual"))
  ,)

;;; Run Day 10
(comment
  (def parser-10 (day-parser "day-10" day-10/parse-machines))
  (parser-10 "example")

  (day-10/solve-part-1 (parser-10 "example"))
  (day-10/solve-part-1 (parser-10 "actual"))

  (day-10/solve-part-2 (parser-10 "example"))
  (day-10/solve-part-2 (parser-10 "actual"))
  ,)

;;; Run Day 11
(comment
  (def parser-11 (day-parser "day-11" day-11/parse-devices))
  (parser-11 "example")

  (day-11/solve-part-1 (parser-11 "example"))
  (day-11/solve-part-1 (parser-11 "actual"))

  (day-11/solve-part-2 (parser-11 "example-2"))
  (day-11/solve-part-2 (parser-11 "actual"))
  ,)
