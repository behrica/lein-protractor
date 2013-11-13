(ns leiningen.protractor-test
    (:require  [midje.sweet :refer :all]
               [leiningen.protractor :refer :all]
               [leiningen.core.main :refer [abort]]
               [clojure.java.shell :refer [sh]]
               [leiningen.ring.server :refer [server-task]]))

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


; fails ...   see https://github.com/marick/Midje/issues/251
(fact "passing a port uses it"
  (protractor project) => nil
  (provided
    (start-ring project protractor-config) => nil
    (abort "Protractor failed")  => nil :times 0
    (wait-a-bit) => ""
    (sh-protractor protractor-config) => nil
))

