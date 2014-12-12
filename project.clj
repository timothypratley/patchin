(defproject patchin "0.2.3"
  :description "Creates and applies patches to datastructures"
  :url "http://github.com/timothypratley/patchin"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :aliases {"cleantest"  ["do"  "clean,"  "test,"  "cljsbuild"  "test"]}
  :profiles {:dev {:plugins [[com.keminglabs/cljx "0.5.0"]]}}
  :source-paths ["target/generated/src"]
  :prep-tasks  [["cljx" "once"]]
  :cljx {:builds [{:source-paths ["srcx" "test"]
                   :rules :clj
                   :output-path "target/generated/src"}
                  {:source-paths ["srcx" "test"]
                   :rules :cljs
                   :output-path "target/generated/src"}]})
