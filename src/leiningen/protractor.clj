(ns leiningen.protractor
  (:require [clojure.java.shell :refer [sh]]
            [ring.server.leiningen :refer [serve]]
            [leiningen.ring.server :refer :all ]
            [leiningen.core.main :refer [abort]])
  (:import org.openqa.selenium.server.SeleniumServer))


(defn- sh-protractor [protractor]
  (let [ret (sh "protractor" (:protractorconfig protractor))]
     (println (:out ret))
     (println (:err ret))
     (if (not (= 0 (:exit ret)))
         (abort "Protractor failed")))
)

(defn wait-a-bit []
  (Thread/sleep 20000))

(defn protractor
  "Starts/stop/runs selenium server, ring server, protractor"
  [project & args]
  ;{ :pre (not (nil? (:protractor project)))}
  (let [protractor (:protractor project)]
    (System/setProperty "webdriver.chrome.driver" (:chromedriver protractor))
    (let [selenium-server (org.openqa.selenium.server.SeleniumServer.)]
      (.start selenium-server)
      (future (server-task project {:join? false :port 8080 :open-browser? false :init (:init protractor)}))
      (wait-a-bit)
      (sh-protractor protractor)
      (.stop selenium-server))))
