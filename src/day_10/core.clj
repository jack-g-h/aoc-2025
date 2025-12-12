(ns day-10.core
  (:require
    [clojure.string :as string]
    [libpython-clj2.require :refer [require-python]]
    [libpython-clj2.python :refer [py. py.. py.-] :as py]))

(defn parse-line
  [line]
  (let [line-parts (string/split line #" ")
        first-part (first line-parts)
        last-part (last line-parts)
        rest-parts (drop 1 (drop-last 1 line-parts))

        light-goal (mapv {\. false \# true}
                         (subs first-part 1 (dec (count first-part))))
        buttons (mapv (fn [button-str]
                        (let [sans-parens (subs button-str 1 (dec (count button-str)))
                              values (string/split sans-parens #",")]
                          (mapv #(Long/parseLong %) values)))
                      rest-parts)
        joltages (mapv #(Long/parseLong %)
                       (string/split (subs last-part 1 (dec (count last-part))) #","))]
    {:required-lights light-goal
     :button-wirings buttons
     :joltage-requirements joltages}))

(defn parse-machines
  [file-lines]
  (mapv parse-line file-lines))

(defn apply-button
  [light-state button-wiring]
  (reduce #(update %1 %2 not) light-state button-wiring))
(def apply-button (memoize apply-button))

(defn minimum-presses
  [machine]
  (loop [states [(into [] (repeat (count (:required-lights machine)) false))]
         iterations 0]
    (cond
      (some (partial = (:required-lights machine)) states) iterations
      :else (recur (reduce into (mapv (fn [state]
                                        (mapv (partial apply-button state) (:button-wirings machine)))
                                      states))
                   (inc iterations)))))

(defn solve-part-1
  [machines]
  (reduce + (mapv minimum-presses machines)))

(require-python 'scipy.optimize)

(defn make-equations
  [machine]
  (let [initial-matrix (into [] (repeat (count (:joltage-requirements machine))
                                        (into [] (repeat (count (:button-wirings machine)) 0))))]
    (reduce (fn [matrix [button-num button-vals]]
              (reduce #(update-in %1 [%2 button-num] inc)
                      matrix
                      button-vals))
            initial-matrix
            (mapv (partial conj [])
                  (range)
                  (:button-wirings machine)))))

(defn power-machine
  [machine]
  (let [weights (into []
                      (repeat (count (:button-wirings machine)) 1))
        equations (make-equations machine)
        integrality (into []
                          (repeat (count (:button-wirings machine)) 1))]
    (int (py.- (scipy.optimize/linprog :c weights :A_eq equations :b_eq (:joltage-requirements machine) :integrality integrality) :fun))))

(defn solve-part-2
  [machines]
  (reduce + (mapv power-machine machines)))