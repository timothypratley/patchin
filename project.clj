(defproject patchin "0.2.0"
  :description "Creates and applies patches to datastructures"
  :url "http://github.com/timothypratley/patchin"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :aliases {"build-once" ["cljx" "once"]
            "deploy-lib" ["do" "build-once," "deploy" "clojars"]}
  :profiles {:dev {:plugins [[com.keminglabs/cljx "0.4.0" :exclusions [org.clojure/clojure]]]}}
  :cljx {:builds [{:source-paths ["src" "test"]
                   :rules :clj
                   :output-path "target/classes"}
                  {:source-paths ["src" "test"]
                   :rules :cljs
                   :output-path "target/classes"}]})
