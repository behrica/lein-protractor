(ns leiningen.protractor-test
    (:require  [midje.sweet :refer :all]
               [leiningen.protractor :refer :all]
               [leiningen.core.main :refer [abort]]
               [clojure.java.shell :refer [sh]]
               [leiningen.ring.server :refer [server-task]]))

(defn deep-merge-with
  "Like merge-with, but merges maps recursively, applying the given fn
  only when there's a non-map at a particular level.

  (deepmerge + {:a {:b {:c 1 :d {:x 1 :y 2}} :e 3} :f 4}
               {:a {:b {:c 2 :d {:z 9} :z 3} :e 100}})
  -> {:a {:b {:z 3, :c 3, :d {:z 9, :x 1, :y 2}}, :e 103}, :f 4}"
  [f & maps]
  (apply
    (fn m [& maps]
      (if (every? map? maps)
        (apply merge-with m maps)
        (apply f maps)))
    maps))




(defn noop-handler [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body ""})

(defn noop [])

(def protractor-config
  {:init noop
   :chromedriver "chromedriver"
   :protractorconfig "a.js"
   }
  )

(def project { :ring {:handler noop-handler}
               :protractor protractor-config})

(fact "when shell out to protactor call fails, abort() is called"
    (protractor project) => nil
    (provided
      (abort "Protractor failed")  => nil
      (sh "protractor" "a.js") => {}
      (wait-a-bit) => nil))

(fact "when shell out to protactor call works, abort() is not called"
  (protractor project) => nil
  (provided
    (abort "Protractor failed")  => nil :times 0
    (sh "protractor" "a.js") => {:exit 0}
    (wait-a-bit) => nil))



(fact "throws when called with misisng data"
  (protractor {}) => (throws AssertionError)
  (protractor {:protractor {}}) => (throws AssertionError))

;(fact "passing a port uses it"
;  (protractor project) => nil
;  (provided
;    (start-ring project protractor-config) => nil
;    (abort "Protractor failed")  => nil :times 0
;    (sh-protractor protractor-config) => nil
;    (wait-a-bit) => nil
;  ))
