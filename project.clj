(defproject patchin "0.1.0"
  :description "Creates and applies patches to datastructures"
  :url "http://github.com/patchin"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :profiles {:dev {:dependencies [[criterium "0.4.3"]
                                  [org.clojure/test.check "0.5.9"]]}})
