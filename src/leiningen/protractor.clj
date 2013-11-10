(ns leiningen.protractor
  (:require
            [clojure.java.shell :refer [sh]]
            [ring.server.leiningen :refer [serve]]
            [leiningen.ring.server :refer :all ]
            )
  (:import org.openqa.selenium.server.SeleniumServer)

  )




(defn protractor
  "Starts/stop/runs selenium server, ring server, protractor"
  [project & args]
  (let [protractor (:protractor project)]
    (System/setProperty "webdriver.chrome.driver" (:chromedriver protractor ))
    (let [selenium-server (org.openqa.selenium.server.SeleniumServer.)]
      (.start selenium-server)
      (future (server-task project {:join? false :port 8080 :open-browser? false :init (:init protractor)}))
      (Thread/sleep 20000)
      (println (sh "protractor" (:protractorconfig protractor)))
      (.stop selenium-server))))
