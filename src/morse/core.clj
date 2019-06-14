(ns morse.core
  (:gen-class)
  (:require [clojure.set :refer [map-invert]]
            [clojure.string :as str]
            [clojure.java.io :as io]
            [morse.codes :as codes]))

(defn encode
  [lang message]
  "Translate natural language string into Morse code"
  (let [codes (lang codes/lang->codes)]
    (str/join (map #(get codes %) message))))

(defn- dict->tree
  [path dict-entries dict]
  (if (= 0 (count dict-entries))
    nil
    (let [next-dot-path (str path \.)
          next-dash-path (str path \-)]
      {:value (get dict path)
       :branches {
                  \- (dict->tree
                      next-dash-path
                      (filter
                       #(str/starts-with? % next-dash-path)
                       dict-entries)
                      dict)
                  \. (dict->tree
                      next-dot-path
                      (filter
                       #(str/starts-with? % next-dot-path)
                       dict-entries)
                      dict)}
       }
      )))

(defn- build-decoding-tree
  [lang]
  (let [file-name (if (= :ru lang)
                    "ru_dict.txt"
                    "en_dict.txt")
        words-file (io/resource file-name)
        words (->
               (slurp words-file)
               (str/lower-case)
               (str/split #"\n"))
        encoded-words (map #(encode lang %) words)
        dict (zipmap encoded-words words)]
    (dict->tree "" encoded-words dict)))

(defn- find-messages
  [code decoding-tree sub-tree message]
  (let [code-char (first code)
        rest-code (rest code)
        sub-tree (get (:branches sub-tree) code-char)
        word (:value sub-tree)]
    (cond
      (and (empty? rest-code) (empty? word)) []
      (empty? rest-code) [(conj message word)]
      (empty? word) (find-messages rest-code decoding-tree sub-tree message)
      :else (into (find-messages rest-code decoding-tree sub-tree message)
                  (find-messages rest-code decoding-tree decoding-tree (conj message  word))))))

(defn decode
  "Find all possible input messages in a given language matching given Morse code"
  [lang code]
  (let [decoding-tree (build-decoding-tree lang)
        matching-messages (find-messages code decoding-tree decoding-tree [])
        most-meaningful (take 50 (sort-by count matching-messages))]
    most-meaningful))

(defn- count-messages
  [code code->symbol]
  (if (empty? code) 1
      (let [codes (keys code->symbol)
            next-codes (filter #(str/starts-with? code %) codes)]
        (reduce + (map #(count-messages (subs code (count %)) code->symbol)
                       next-codes)))))

(defn messages-amount
  "Find the amount of all possible input messages matching given Morse code"
  [lang code]
  (let [symbol->code (lang codes/lang->codes)
        code->symbol (map-invert symbol->code)]
    (count-messages code code->symbol)))

(defn -main
  [& args]
  (let [lang (keyword (first args))
        code (second args)
        lel (decode lang code)
        messages (map #(str/join " " %) (decode lang code))]
    (println "Possible messages:")
    (println (str/join "\n" messages))))
