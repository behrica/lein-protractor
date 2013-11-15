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
   :config "a.js"
   :wait 4000 
   }
  )

(def project { :ring {:handler noop-handler}
               :protractor protractor-config})

(fact "when shell out to protactor call fails, abort() is called"
    (protractor project) => nil
    (provided
      (new-selenium-server) => "aServer"
      (start-selenium "aServer")  => nil
      (abort "Protractor failed")  => nil
      (sh "protractor" "a.js") => {}
      (wait-a-bit 4000) => nil
      (stop-selenium "aServer") => nil
))

(fact "when shell out to protactor call works, abort() is not called"
  (protractor project) => nil
  (provided
    (new-selenium-server) => "aServer"
    (start-selenium "aServer")  => nil
    (start-ring-with-future project noop 8080) => nil
    (abort "Protractor failed")  => nil :times 0
    (sh "protractor" "a.js") => {:exit 0}
    (wait-a-bit 4000) => nil
    (stop-selenium "aServer") => nil))


(fact "when no :wait specified, 20000 is used"
  (let [project-without-wait           (assoc-in project [:protractor :wait] nil)
        protractor-config-without-wait (assoc-in protractor-config [:wait] nil )]
  (protractor project-without-wait) => nil
  (provided
    (new-selenium-server) => "aServer"
    (start-selenium "aServer")  => nil
    (start-ring-with-future project-without-wait noop 8080) => nil
    (abort "Protractor failed")  => nil :times 0
    (sh "protractor" "a.js") => {:exit 0}
    (wait-a-bit 20000) => nil
    (stop-selenium "aServer") => nil))) 



  
(fact "throws when called with misisng data"
  (protractor {}) => (throws AssertionError)
  (protractor {:protractor {}}) => (throws AssertionError))


(fact "passing a port uses it"
  (let [project-with-port (assoc-in project [:ring :port] 2000)]
    (protractor project-with-port) => nil
    (provided   
      (new-selenium-server) => "aServer"
      (start-selenium "aServer")  => nil
      (start-ring-with-future project-with-port noop 2000) => nil
      (abort "Protractor failed")  => nil :times 0
      (wait-a-bit 4000) => ""
      (sh-protractor protractor-config) => nil
      (stop-selenium "aServer") => nil)))

