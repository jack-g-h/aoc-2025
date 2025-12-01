(ns core
  (:require [day-1.core :as day-1]
            [clojure.java.io :as io]))

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