(ns patchin
  "Creates and applies patches to datastructures."
  (:require [clojure.data :as data]))

(defn discard
  "Like dissoc, but also works for sets and sequences."
  [m k]
  (cond
   (set? m) (disj m k)
   (associative? m) (dissoc m k)
   (seq? m) (concat (take k m) (drop (inc k) m))
   :else m))

(defn dissoc-in
  "Dissociates an entry from a nested associative structure returning a new
  nested structure. keys is a sequence of keys. Any empty maps that result
  will not be present in the new structure."
  [m [k & ks :as keys]]
  (if ks
    (if-let [nextmap (get m k)]
      (let [newmap (dissoc-in nextmap ks)]
        (if (seq newmap)
          (assoc m k newmap)
          (discard m k)))
      m)
    (discard m k)))

(defn paths
  "Creates a sequence of key paths for nested maps."
  [m]
  (cond
   (set? m) (for [k m]
              [k])
   (map? m) (for [[k v] m
                  tail (or (paths v) [nil])]
              (cons k tail))))
;; TODO: find set paths, and make leaves sets

(defn strip
  "Dissocs all key paths in remove from m."
  [m remove]
  (reduce dissoc-in m (paths remove)))

(defn deep-merge
  "Recursively merges maps. If not maps, the last value wins."
  [& vals]
  (if (every? map? vals)
    (apply merge-with deep-merge vals)
    (last vals)))

(defn patch
  "Updates a map by removal of keys and addition of values."
  [m [remove add]]
  (deep-merge (strip m remove) add))

(defn disses
  "Given nested maps of keys to remove and add,
  calculates the nested sequence of keys
  that need to be dissoced."
  [remove add]
  (when (map? remove)
    (when-let [s (seq (for [[k v] remove
                            :let [replace (get add k)
                                  more (disses v replace)]
                            :when (or more (not replace))]
                        [k (or more 1)]))]
      (into {} s))))

(defn diff
  "Creates a patch that can be applied with patch.
  A patch is [discards additions]"
  [a b]
  (let [[remove add] (data/diff a b)
        p [(disses remove add) add]]
    (assert (= b (patch a p))
            "Failed to create patch")
    p))

(defn smaller?
  "Is patch p smaller than the final state m?"
  [p m]
  (< (count (pr-str p)) (count (pr-str m))))
