(defproject lein-protractor "0.1.1-SNAPSHOT"
  :description "Leiningen Protractor plugin"
  :url "https://github.com/behrica/lein-protractor"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.seleniumhq.selenium/selenium-server "2.29.1"]
                 [lein-ring "0.8.8"]
                 [ring "1.2.1"]
                  [ring-server/ring-server "0.3.1"]
                  ;
                 ]
  :eval-in-leiningen true
  :plugins [[lein-release "1.1.3"][lein-midje "3.1.2"][lein-ancient "0.5.3"]]
  :lein-release {:deploy-via :lein-install}

  :profiles {:dev { :dependencies [[midje "1.5.1"][leiningen "2.3.3"]]}}

  :aliases {"build" ["do" "clean," "install," "midje"]}
)


