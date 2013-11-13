(ns leiningen.protractor
  (:require [clojure.java.shell :refer [sh]]
            [ring.server.leiningen :refer [serve]]
            [leiningen.ring.server :refer :all ]
            [leiningen.core.main :refer [abort]])
  (:import org.openqa.selenium.server.SeleniumServer))



(defn sh-protractor [protractor-config]
  (let [ret (sh "protractor" (:protractorconfig protractor-config))]
     (println (:out ret))
     (println (:err ret))
     (if (not (= 0 (:exit ret)))
         (abort "Protractor failed")))
)

(defn wait-a-bit []
  (Thread/sleep 20000))

(defn- keys-present? [m keys]
  (get-in m keys)
)


(defn start-ring [project protractor-config]
  (server-task project {:join? false :port 8080 :open-browser? false :init (:init protractor-config)})
  )

(defn protractor
  "Starts/stop/runs selenium server, ring server, protractor"
  [project & args]
  {:pre [(keys-present?  project [:protractor :chromedriver])]}

  (let [protractor-config (:protractor project)]
    (System/setProperty "webdriver.chrome.driver" (:chromedriver protractor-config))
    (let [selenium-server (org.openqa.selenium.server.SeleniumServer.)]
      (.start selenium-server)
      (future (start-ring project protractor-config))
      (wait-a-bit)
      (sh-protractor protractor-config)
      (.stop selenium-server))))
