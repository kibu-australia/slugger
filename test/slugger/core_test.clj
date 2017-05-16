(ns slugger.core-test
  (:use clojure.test
        slugger.core))

(deftest simple-slug-test
  (testing "simple slugification"
    (is (= (->slug " this string   should be simple enough") "this-string-should-be-simple-enough"))))

(deftest kanji-test
  (testing "kanji slugs"
    (is (= (->slug "learn how to say 你好") "learn-how-to-say-ni-hao"))))

(deftest currency-test
  (testing "currency slugs"
    (is (= (->slug "An idea worth $100") "an-idea-worth-100-dollars"))
    (is (= (->slug "An idea worth ¥100") "an-idea-worth-100-yen"))
    (is (= (->slug "An idea worth £100") "an-idea-worth-100-pounds"))
    (is (= (->slug "An idea worth €100") "an-idea-worth-100-euros"))))

(deftest o-slash-test
  (testing "Scandinavian o-slash"
    (is (= (->slug "Vi vil have mere &Oslash;l")  "vi-vil-have-mere-oel"))
    (is (= (->slug "Vi vil have mere øl")         "vi-vil-have-mere-oel"))))

(deftest email-address-test
  (testing "email address slugification"
    (is (= (->slug "my email address is pelle@picomoney.com") "my-email-address-is-pelle-at-picomoney-dot-com"))))
