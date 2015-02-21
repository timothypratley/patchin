(ns timothypratley.patchin
  "Creates and applies patches to datastructures."
  (:require
   [clojure.data :as data]
   [clojure.set :refer [union]]))

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
  nested structure."
  [m [k & ks :as keys]]
  (if ks
    (if-let [nextmap (get m k)]
      (assoc m k (dissoc-in nextmap ks))
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

(defn strip
  "Dissocs all key paths from m."
  [m remove]
  (reduce dissoc-in m (paths remove)))

(defn deep-merge
  "Recursively merges maps. Unions sets. Last value wins."
  [& vals]
  (cond
   (every? map? vals)
   (apply merge-with deep-merge vals)

   (every? set? vals)
   (apply union vals)

   :else
   (last vals)))

(defn patch
  "Updates a map by removal of keys and addition of values."
  [m [remove add]]
  (if add
    (deep-merge (strip m remove) add)
    remove))

(defn disses
  "Given nested maps of keys to remove and add,
  calculates the nested sequence of keys
  that need to be dissoced."
  [remove add]
  (cond
    (map? remove) (when-let [s (seq (for [[k v] remove
                                          :let [replace (get add k)
                                                more (disses v replace)]
                                          :when (or more (not replace))]
                                      [k (or more 1)]))]
                    (into {} s))
    (set? remove) remove))

(defn smaller?
  "Is patch p smaller than the final state m?"
  [p m]
  (< (count (pr-str p)) (count (pr-str m))))

(defn dak
  "Diff associative things a and b, comparing only the key k."
  [a b k]
  (let [va (get a k)
        vb (get b k)
        [a* b* ab] (clojure.data/diff va vb)
        in-a (contains? a k)
        in-b (contains? b k)
        same (and in-a in-b
                  (or (not (nil? ab))
                      (and (nil? va) (nil? vb))))]
    [(when (and in-a (or (not (nil? a*)) (and (not same) (nil? va)))) {k a*})
     (when (and in-b (or (not (nil? b*)) (and (not same) (nil? vb)))) {k b*})
     (when same {k ab})]))

(defn diff
  "Creates a patch that can be applied with patch.
  A patch is [discards additions].
  discards is a map of keys to remove, possibly nested.
  additions can be a value that replaces the existing value,
  or a map, possibly nested, of values to add or replace.
  Sequences are treated as values."
  [a b]
  ;; TODO: use a better seq diff
  ;; TODO: sequences of maps
  (with-redefs [data/diff-sequential #+clj (var data/atom-diff) #+cljs data/atom-diff
                data/diff-associative-key dak]
    (let [[remove add] (data/diff a b)
          p [(disses remove add) (or add {})]
          success (= b (patch a p))]
      (when-not success
        (prn "Patch failed: " a b p))
      (if (and success (smaller? p [b]))
        p
        [b]))))
