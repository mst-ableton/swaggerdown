(defproject swaggerdown "0.1.0-SNAPSHOT"
  :description "An application for producing static documentation from json"
  :url "http://github.com/markwoodhall/swaggerdown"
  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}

  :min-lein-version "2.7.1"

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.9.946"]
                 [org.clojure/core.async  "0.3.443"]
                 [aleph "0.4.3"]
                 [aero "1.1.2"]
                 [bidi "2.1.2"]
                 [com.stuartsierra/component "0.3.1"]
                 [com.taoensso/timbre "4.10.0"]
                 [cheshire "5.6.3"]
                 [clova "0.35.2"]
                 [clj-ravendb "1.3.1"]
                 [io.forward/yaml "1.0.6"]
                 [marge "0.14.0" :exclusions [[org.clojure/clojure] [org.clojure/clojurescript]]]
                 [markdown-clj "1.0.1"]
                 [reagent "0.7.0" :exclusions [[org.clojure/clojure] [org.clojure/clojurescript]]]
                 [selmer "1.11.3" ]
                 [tick "0.3.5"]
                 [yada "1.2.9"]]

  :plugins [[lein-figwheel "0.5.18"]
            [lein-cljsbuild "1.1.7" :exclusions [[org.clojure/clojure]]]]

  :main swaggerdown.app
  :target-path "target/%s"
  :uberjar-name "swaggerdown.jar"

  :source-paths ["src"]

  :cljsbuild {:builds
              [{:id "dev"
                :source-paths ["src"]

                ;; The presence of a :figwheel configuration here
                ;; will cause figwheel to inject the figwheel client
                ;; into your build
                :figwheel {:on-jsload "swaggerdown.client/on-js-reload"
                           :websocket-host :js-client-host
                           ;; :open-urls will pop open your application
                           ;; in the default browser once Figwheel has
                           ;; started and compiled your application.
                           ;; Comment this out once it no longer serves you.
                           :open-urls ["http://localhost:3080/index.html"]}

                :compiler {:main swaggerdown.client
                           :asset-path "js/compiled/out"
                           :output-to "resources/public/js/compiled/swaggerdown.js"
                           :output-dir "resources/public/js/compiled/out"
                           :source-map-timestamp true
                           :verbose true
                           ;; To console.log CLJS data-structures make sure you enable devtools in Chrome
                           ;; https://github.com/binaryage/cljs-devtools
                           :preloads [devtools.preload]}}
               ;; This next build is a compressed minified build for
               ;; production. You can build this with:
               ;; lein cljsbuild once min
               {:id "min"
                :source-paths ["src"]
                :compiler {:output-to "resources/public/js/compiled/swaggerdown.js"
                           :main swaggerdown.client
                           :optimizations :simple
                           :pretty-print false}}]}

  :figwheel {;; :http-server-root "public" ;; default and assumes "resources"
             ;; :server-port 3449 ;; default
             ;; :server-ip "127.0.0.1"

             :css-dirs ["resources/public/css"] ;; watch and update CSS

             ;; Start an nREPL server into the running figwheel process
             ;; :nrepl-port 7888

             ;; Server Ring Handler (optional)
             ;; if you want to embed a ring handler into the figwheel http-kit
             ;; server, this is for simple ring servers, if this

             ;; doesn't work for you just run your own server :) (see lein-ring)

             ;; :ring-handler hello_world.server/handler

             ;; To be able to open files in your editor from the heads up display
             ;; you will need to put a script on your path.
             ;; that script will have to take a file path and a line number
             ;; ie. in  ~/bin/myfile-opener
             ;; #! /bin/sh
             ;; emacsclient -n +$2 $1
             ;;
             ;; :open-file-command "myfile-opener"

             ;; if you are using emacsclient you can just use
             ;; :open-file-command "emacsclient"

             ;; if you want to disable the REPL
             ;; :repl false

             ;; to configure a different figwheel logfile path
             ;; :server-logfile "tmp/logs/figwheel-logfile.log"

             ;; to pipe all the output to the repl
             ;; :server-logfile false
             }


  ;; Setting up nREPL for Figwheel and ClojureScript dev
;; Please see:
;; https://github.com/bhauman/lein-figwheel/wiki/Using-the-Figwheel-REPL-within-NRepl
:profiles {:dev {:dependencies [[binaryage/devtools "0.9.4"]
                                [figwheel-sidecar "0.5.18"]
                                [cider/piggieback "0.4.1"]
                                [reloaded.repl "0.2.3"]
                                [org.clojure/tools.namespace "0.2.11"]]
                 :uberjar {:aot :all}
                 ;; need to add dev source path here to get user.clj loaded
                 :source-paths ["src" "dev"]
                 ;; for CIDER
                 ;; :plugins [[cider/cider-nrepl "0.12.0"]]
                 :repl-options {:nrepl-middleware [cider.piggieback/wrap-cljs-repl]
                                :timeout 1200000}
                 ;; need to add the compliled assets to the :clean-targets
                 :clean-targets ^{:protect false} ["resources/public/js/compiled"
                                                   :target-path]}
           :prod {:uberjar {:aot :all}
                  :source-paths ["src"]}
           :uberjar {:aot :all
                     :source-paths ["src"]}})
