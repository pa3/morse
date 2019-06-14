(defproject morse "0.1.0"
  :description "Decode Morse code missing chars/words separators"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]]
  :repl-options {:init-ns morse.core}
  :main morse.core)
