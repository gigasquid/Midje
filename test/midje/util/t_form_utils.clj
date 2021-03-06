;; -*- indent-tabs-mode: nil -*-

(ns midje.util.t-form-utils
  (:use [midje.util.form-utils])
  (:use [midje.sweet])
  (:use [midje.test-util]))

(defrecord R [a])

(facts "about recognizing classic maps"
  (classic-map? {}) => truthy
  (classic-map? (R. 1)) => falsey
  (classic-map? 1) => falsey)

(facts "about recognizing records"
  (record? {}) => falsey
  (record? (R. 1)) => truthy
  (record? 1) => falsey)

(facts "a form's reader-assigned line-number can be extracted"
  (reader-line-number (with-meta '(fact (this that)) {:line 23})) => 23
  "or, failing that: try top-level subforms"
  (reader-line-number `(fact
                         (+ 1 2)
                         ~(with-meta '(this that) {:line 23})
                         ~(with-meta '(this that) {:line 22223}))) => 23
  "or a default value"
  (reader-line-number (with-meta '(fact "text") {})) => "0 (no line info)")

(facts "sometimes it's useful to flatten and remove nils"
  (flatten-and-remove-nils '()) => []
  (flatten-and-remove-nils '(nil "foo" ("bar" nil "baz"))) => ["foo" "bar" "baz"])

(facts "extract elements from vectors and return remainder"
  (vector-without-element-at-index 0 [0 1 2]) => vector?
  (vector-without-element-at-index 0 [0 1 2]) => [1 2]
  (vector-without-element-at-index 1 [0 1 2]) => [0 2]
  (vector-without-element-at-index 2 [0 1 2]) => [0 1])


(facts "you can tack new keys onto a hashmap"
  (tack-on-to {:a [1], :b [55] :c 'blah} :a 2 :b 56) => {:a [1 2], :b [55 56], :c 'blah})

(fact "pairs are exciting"
  (pairs [:a :b :c] [1 2 3]) => [ [:a 1] [:b 2] [:c 3] ])

(fact "it can be useful to get hash-map to allow duplicates"
  (hash-map-duplicates-ok) => {} 
  (hash-map-duplicates-ok :a 1 :b 2) => {:a 1 :b 2}
  (hash-map-duplicates-ok :a 1 :b 2 :b 33333) => {:a 1 :b 33333})

