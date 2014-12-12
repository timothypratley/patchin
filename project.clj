(defproject patchin "0.2.2"
  :description "Creates and applies patches to datastructures"
  :url "http://github.com/timothypratley/patchin"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :aliases {"cleantest"  ["do"  "clean,"  "cljx"  "once,"  "test,"  "cljsbuild"  "test"]}
  :profiles {:dev {:plugins [[com.keminglabs/cljx "0.5.0"]]}}
  :cljx {:builds [{:source-paths ["src" "test"]
                   :rules :clj
                   :output-path "target/classes"}
                  {:source-paths ["src" "test"]
                   :rules :cljs
                   :output-path "target/classes"}]})
