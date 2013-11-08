(ns leiningen.protractor
  (:require
            [clojure.java.shell :refer [sh]]
            [ring.server.leiningen :refer [serve]]
            [leiningen.ring.server :refer :all ]
            )
  (:import org.openqa.selenium.server.SeleniumServer)

  )


(defn app [request]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body "Hello World"})

(defn protractor
  "Starts/stop/runs selenium server, ring server, protractor"
  [project & args]
  (System/setProperty "webdriver.chrome.driver" "/home/carsten/bin/chromedriver")
  (let [selenium-server (org.openqa.selenium.server.SeleniumServer.)]
    (.start selenium-server)
    (println "before serve start")
    (future (server-task project {:join? false :port 8080 :open-browser? false :init (:init-db (:protractor project))}))
    (println "after server strt")
    (Thread/sleep 20000)
    (println (sh "protractor" "resources/protractor_conf.js"))
    (.stop selenium-server))
)
