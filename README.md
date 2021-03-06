# lein-protractor

A Leiningen plugin to execute AngularJS integration tests with Protractor (https://github.com/angular/protractor)

This plugins support the idea of integration tests so it does up to three things:

1. Starting a selenium server
2. Starting your app server with the leiningen ring plugin
   3. This might call a init method, which can be used to setup teh database for example

-> Afterwards it shuts all down

## Usage


Put `[lein-protractor "0.1.1-SNAPSHOT"]` into the `:plugins` vector of your project.clj.


    $ lein protractor

It can be configured by this keys in project.clj:

    :ring {
         :handler todo.web/app  ; handler function, which will be started by ring server
         :port 8080             ; port to start ring server on 
     }       
    :protractor {
         :init todo.db/init-db  ; function to be executed by ring plugin when ring server starts
         :chromedriver "/home/xxxxx/bin/chromedriver"
         :config "resources/protractor_conf.js"
         :wait Time to wait in ms, before starting protractor. This should be the expected time itg takes to start the ring server and run the init method 
    }


## License

Copyright © 2013 Carsten Behring

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
