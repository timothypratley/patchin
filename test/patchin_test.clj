(ns patchin-test
  (:require [patchin :refer :all]
            [clojure.test :refer :all]))

(deftest test-dissoc-in
  (is (= {:a #{2 3}}
         (dissoc-in {:a #{1 2 3}} [:a 1]))))

(deftest test-paths
  (is (= [[:foo :baz :boz] [:foo :bar]]
         (paths {:foo {:bar 1
                       :baz {:boz :big}}})))
  (is (= [[:foo 1]]
         (paths {:foo #{1}}))))

(deftest test-strip
  (is (= {:foo {:baz 2}}
         (strip {:foo {:bar 1
                         :baz 2}}
                  {:foo {:bar 1}}))))

(deftest test-disses
  (is (= {:foo {:bar 1
                :baz {:boz 1}}}
         (disses {:foo {:bar "astring"
                        :baz {:boz 5}}}
                 nil)))
  (is (= {:foo {:bar 1}}
         (disses {:foo {:bar "astring"
                        :baz "astring"}}
                 {:foo {:baz "boz"}}))))

;; TODO: implement sequence patching
#_(deftest test-patch
  (let [a {:foo {:bar 1}
           :baz 3}
        b {:foo {:bar 2}}
        [remove add] (clojure.data/diff a b)
        d (disses remove add)]
    (is (= {:baz 1} d))
    (is (= b (patch a [d add]))
        "should patch simple map"))
  (let [x {:a #{1 2 3}
           :b {:c ["q" "wer" "ty"]
               :d "dvorak"
               100 101
               "fast" :car}
           nil nil
           :d {false [false false]}}
        y {:a #{2 3 4}
           :b {:c ["q"]
               :z "addedz"}
           nil true
           :d {false false}}
        p (diff x y)]
    (is (= y (patch x p))
        "should patch complex map")
    (is (smaller? p y)
        "should patch efficiently")))
