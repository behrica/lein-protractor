(ns leiningen.protractor-test
    (:require  [midje.sweet :refer :all]
               [leiningen.protractor :refer :all]
               [leiningen.core.main :refer [abort]]
               [clojure.java.shell :refer [sh]]
               ))

(defn noop-handler [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body ""})

(defn noop [])

(def project { :ring {:handler noop-handler}
               :protractor {:init noop
                            :chromedriver "chromedriver"
                            :protractorconfig "a.js"
                            }})

(fact "if shell out to protactor call fails, abort() is called"
    (protractor project) => nil
    (provided (abort "Protractor failed")  => nil)
    (provided (sh "protractor" "a.js") => {})
    (provided (wait-a-bit) => nil))

(fact "if shell out to protactor call works, abort() is not called"
  (protractor project) => nil
  (provided (abort "Protractor failed")  => nil :times 0)
  (provided (sh "protractor" "a.js") => {:exit 0})
  (provided (wait-a-bit) => nil))

