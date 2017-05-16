(ns slugger.core
  (:refer-clojure :exclude [replace])
  (:require [slugger.conversions :as c]
            [clojure.string :refer [replace lower-case trim]]))

(defn ->slug
  "Convert a UTF-8/16 string into a 7 bit ascii representation suitable for use as a slug in a url."
  [text]
  (-> (c/unidecode text)
      c/convert-accented-entities
      c/convert-misc-entities
      c/convert-misc-characters
      lower-case
      trim
      (replace #"\s+" "-")))
