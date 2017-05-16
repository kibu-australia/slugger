(ns slugger.core
  (:refer-clojure :exclude [replace])
  (:require [slugger.conversions :as c]
            [clojure.string :as s]))

(defn ->slug
  "Convert a UTF-8/16 string into a 7 bit ascii representation suitable for use as a slug in a url."
  [text]
  (-> (c/unidecode text)
      c/convert-accented-entities
      c/convert-misc-entities
      c/convert-misc-characters
      s/lower-case
      s/trim
      (s/replace #"\s+" "-")))
