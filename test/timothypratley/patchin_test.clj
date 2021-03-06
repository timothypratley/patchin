(ns timothypratley.patchin-test
  (:require
   [timothypratley.patchin :refer :all]
   [clojure.data :as data]
   [clojure.test :refer :all]))

(deftest test-discard
  (is (= #{:foo}
         (discard #{:foo :bar} :bar))))

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
                {:foo {:bar 1}})))
  (is (= {:a #{1 2}}
         (strip {:a #{1 2 3}} {:a #{3}}))))

(defn disses-diff [a b]
  (let [[add remove] (data/diff a b)]
    (disses a b add remove)))

(deftest test-disses
  (let [a {:s "astring"
           :m {:n 5}}
        b {:s "astring"
           :m {}}
        c {:s "astring"}
        d {:s "astring"
           :m "boz"}]
    (is (= {:m {:n 1}}
           (disses-diff a b)))
    (is (= {:m 1}
           (disses-diff a c)))
    (is (= {}
           (disses-diff a d)))))

(deftest test-value-patch
  (let [a 1
        b 2
        p (diff a b)]
    (is (= b (patch a p))))
  (let [a [1 2]
        b [1 2 3]
        p (diff a b)]
    (is (= b (patch a p)))))

(deftest test-simple-patch
  (let [a {:foo {:bar 1}
           :baz 3}
        b {:foo {:bar 2}}
        [remove add] (clojure.data/diff a b)
        d (disses a b remove add)]
    (is (= {:baz 1} d))
    (is (= b (patch a [d add]))
        "should patch simple map")))

(deftest test-set-patch
  (let [a {:x #{1 2 3}}
        b {:x #{1 2}}
        p (diff a b)]
    (is (= b (patch a p))))
  (let [a #{1 2 3 4 5 6}
        b #{2 3 4 5 6 7}
        p (diff a b)]
    (is (= [#{1} #{7}] p))
    (is (= b (patch a p)))))

(deftest test-complex-patch
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
    (is (not (smaller? [y] p))
        "should patch efficiently")))

(deftest test-nested-removal
  (let [a {:x {:y 9}}
        b {}]
    (is (empty? (with-out-str (diff a b))))))

(deftest test-empty-map
  (let [a {:foo {:bar "fun"}
           :baz "YAHAHAHAHAHAHAHAHAHAHaaa"}
        b {:foo {}
           :baz "YAHAHAHAHAHAHAHAHAHAHaaa"}
        expected-patch [{:foo {:bar 1}} {}]
        p (diff a b)
        warning (with-out-str (diff a b))]
    (is (= b (patch a expected-patch)))
    (is (= expected-patch p))
    (is (empty? warning))))

(deftest test-empty-remove
  (let [a {:foo "bar"
           :baz "booz"}
        b {:foo "bar"}
        p (diff a b)
        expected-p [{:baz 1} {}]]
    (is (= expected-p p))))

(deftest test-nil
  (let [a nil
        b {:foo nil}
        c {:foo 1}]
    (is (empty? (with-out-str (diff a b))))
    (is (empty? (with-out-str (diff c b))))))

(deftest test-nested-nil
  (let [a {:x {:y 1}}
        b {:x {}}
        c {:x nil}]
    (is (empty? (with-out-str (diff a b))))
    (is (empty? (with-out-str (diff a c))))))

(deftest test-smart-nil
  (let [a {:x {:y "22"}, :z "somelargedata"}
        b {:x nil, :z "somelargedata"}
        c {:x {:w "22", :z "somelargedata"}}
        p (diff a b)]
    (is (= [{} {:x nil}] p))
    (is (empty? (with-out-str (diff a c))))))
