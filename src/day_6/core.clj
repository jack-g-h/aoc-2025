(ns day-6.core
  (:require [clojure.string :as string]))

(defn operation-str->function
  [operation-str]
  ({"*" *
    "+" +}
   operation-str))

(defn transpose
  [matrix]
  (mapv (fn [column]
          (mapv #(nth % column) matrix))
        (range (count (first matrix)))))

(defn string-transpose
  [strings]
  (mapv (partial apply str) (transpose strings)))

(defn ensure-length
  "Ensures that all strings within the provided collection are of the same length by
   appending spaces to the shorter strings."
  [strings]
  (let [max-length (apply max (mapv count strings))]
    (mapv (fn [to-ensure]
            (apply str to-ensure (repeat (- max-length (count to-ensure)) " ")))
          strings)))

(defn partition-and-remove
  "Partitions by the provided predicate, and then removes all partitions containing any elements
   that return truthy values under the predicate."
  [predicate collection]
  (filterv (partial not-any? predicate)
           (partition-by predicate collection)))

(defn parse-human-problems
  [file-lines]
  (let [lines (mapv identity file-lines)
        numbers (drop-last lines)
        operations (last lines)]
    {:numbers    (transpose (mapv (fn [row]
                                    (mapv #(Long/parseLong %)
                                          (string/split (string/trim row) #" +")))
                                  numbers))
     :operations (mapv operation-str->function (string/split (string/trim operations) #" +"))}))

(defn parse-cephalopod-problems
  [file-lines]
  (let [lines (mapv identity file-lines)
        numbers (string-transpose (ensure-length (drop-last lines)))
        operations (last lines)]
    {:numbers (mapv (fn [row]
                      (mapv (comp #(Long/parseLong %) string/trim)
                            row))
                    (partition-and-remove (comp empty? clojure.string/trim) numbers))
     :operations (mapv operation-str->function (string/split (string/trim operations) #" +"))}))

(defn solve-homework
  [homework]
  (reduce + (mapv (fn [operation values]
                    (reduce operation values))
                  (:operations homework)
                  (:numbers homework))))
