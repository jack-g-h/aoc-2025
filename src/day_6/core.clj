(ns day-6.core
  (:require [clojure.string :as string]))

(defn operation-str->function
  [operation-str]
  ({"*" *
    "+" +}
   operation-str))

(defn parser-problems
  [file-lines]
  (let [lines (mapv identity file-lines)
        numbers (drop-last lines)
        operations (last lines)]
    {:numbers (mapv (fn [row]
                      (mapv #(Long/parseLong %)
                            (string/split (string/trim row) #" +")))
                    numbers)
     :operations (mapv operation-str->function (string/split (string/trim operations) #" +"))}))

(defn solve-problem
  [homework problem]
  (reduce (nth (:operations homework) problem)
          (mapv #(nth % problem) (:numbers homework))))

(defn solve-part-1
  [homework]
  (let [problem-count (count (:operations homework))]
    (reduce + (mapv (partial solve-problem homework) (range problem-count)))))

(defn ensure-length
  [strings]
  (let [max-length (apply max (mapv count strings))]
    (mapv (fn [to-ensure]
            (if (< (count to-ensure) max-length)
              (apply str to-ensure (repeat (- max-length (count to-ensure)) " "))
              to-ensure))
          strings)))

(defn string-transpose
  [strings]
  (mapv (fn [column]
          (apply str (mapv #(nth % column) strings)))
        (range (count (first strings)))))

(defn parser-cephalopod-problems
  [file-lines]
  (let [lines (mapv identity file-lines)
        numbers (ensure-length (drop-last lines))
        transposed-numbers (string-transpose numbers)
        cephalopod-numbers (filterv (partial not-any? (comp empty? clojure.string/trim))
                                    (partition-by (comp empty? clojure.string/trim)
                                                  transposed-numbers))

        operations (last lines)]
    {:numbers (mapv (fn [row]
                      (mapv (comp #(Long/parseLong %) string/trim)
                            row))
                    cephalopod-numbers)
     :operations (mapv operation-str->function (string/split (string/trim operations) #" +"))}))

(defn solve-part-2
  [cephalopod-homework]
  (reduce + (mapv (fn [operation values]
                    (reduce operation values))
                  (:operations cephalopod-homework)
                  (:numbers cephalopod-homework))))
