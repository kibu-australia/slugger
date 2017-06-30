(ns slugger.conversions
  (:require [clojure.string :as s])
  (:import [net.sf.junidecode Junidecode]))

(defn unidecode
  "Create 7-bit ascii version of unicode string."
  [text]
  (net.sf.junidecode.Junidecode/unidecode text))

(defn convert-accented-entities
  "Convert html accent entities to correct 7bit ascii version"
  [text]
  (s/replace text #"&([A-Za-z])(grave|acute|circ|tilde|uml|ring|cedil|slash);" #(let [d (s/lower-case (second %))]
                                                                                  (cond
                                                                                    (= d "o") "oe"
                                                                                    (= d "a") "aa"
                                                                                    (= d "u") "ue"
                                                                                    :else d))))

(def entity-rules {"#822[01]" "\""
                   "#821[67]" "'"
                   "#8230" "..."
                   "#8211" "-"
                   "#8212" "--"
                   "#215" "x"
                   "gt" ">"
                   "lt" "<"
                   "(#8482|trade)" "(tm)"
                   "(#174|reg)" "(r)"
                   "(#169|copy)" "(c)"
                   "(#38|amp)" "and"
                   "nbsp" " "
                   "(#162|cent)" " cent"
                   "(#163|pound)" " pound"
                   "(#188|frac14)" "one fourth"
                   "(#189|frac12)" "half"
                   "(#190|frac34)" "three fourths"
                   "(#176|deg)" " degrees"})

(defn convert-misc-entities [text]
  (s/replace (reduce #(s/replace %1 (re-pattern (str "&" (key %2) ";")) (val %2)) text entity-rules) #"&[^;]+;" ""))

(defn convert-rules
  "Convert text by applying rules.

  Rules is a map consisting of a regex as the key and the string as the s/replacement value."
  [text rules]
  (reduce #(s/replace %1 (key %2) (val %2)) text rules))

(def currency-rules {#"(\s|^)\$([\d|,| ]+)\.(\d+)(\s|$)" " $2 dollars $3 cents "
                     #"(\s|^)\€([\d|,| ]+)\.(\d+)(\s|$)" " $2 euros $3 cents "
                     #"(\s|^)£([\d|,| ]+)\.(\d+)(\s|$)"  " $2 pounds $3 pence "})

(defn convert-currency
  "Convert misc money values with cents to text."
  [text]
  (convert-rules text currency-rules))

(def normal-rules {#"\s*&\s*" " and "
                   #"\s*#" " number "
                   #"\s*@\s*" " at "
                   #"(\S|^)\.(\S)" "$1 dot $2"
                   #"(\s|^)\$([\d|,| ]*)(\s|$)" " $2 dollars "
                   #"(\s|^)\€([\d|,| ]*)(\s|$)" " $2 euros "
                   #"(\s|^)£([\d|,| ]*)(\s|$)" " $2 pounds "
                   #"(\s|^)¥([\d|,| ]*)(\s|$)" " $2 yen "
                   #"\s*\+\s*" " plus "
                   #"\s*\*\s*" " star "
                   #"\s*%\s*" " percent "
                   #"\s*(\\|\/)\s*" " slash "
                   #"(\s*=\s*)" " equals "})

(defn convert-normal
  "Convert various symbols to spelled out English"
  [text]
  (convert-rules text normal-rules))

(defn convert-misc-characters [text]
  (-> (s/replace text #"\.{3,}" " dot dot dot ")
      (convert-currency)
      (convert-normal)
      (s/replace #"(^|\w)'(\w|$)" "$1$2")
      (s/replace (re-pattern "[.,:;()\\[\\]/\\\\?!^'\"_]") " ")))
