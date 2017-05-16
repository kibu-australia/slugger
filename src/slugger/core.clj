(ns slugger.core
  (:require [slugger.conversions :as c]
            [clojure.string :as s]))

(defn ->slug
  "Convert a UTF-8/16 string into a 7 bit ascii representation suitable for use as a slug in a url."
  [text]
  (-> text
      c/convert-accented-entities
      c/convert-misc-entities
      c/convert-misc-characters
      c/unidecode
      s/lower-case
      s/trim
      (s/replace #"\s+" "-")))
