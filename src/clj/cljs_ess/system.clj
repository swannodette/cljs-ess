(ns cljs-ess.system
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [figwheel.repl :as fig-repl]
            [figwheel.main :as fig-main]
            [cljs.repl :as repl]
            [clojure.core.server :as clj-server]
            [io.pedestal.http :as http]
            [cljs-ess.server :as server]
            [com.stuartsierra.component :as component]))

(def build (-> "dev.cljs.edn" io/file slurp edn/read-string))

(defrecord Server [host port]
  component/Lifecycle
  (start [component]
    (println "Start webserver" host port)
    (let [server (server/create-server host port)]
      (http/start server)
      (assoc component :server server)))
  (stop [{:keys [server] :as component}]
    (http/stop server)
    (dissoc component :server)))

(defn figwheel-repl []
  (repl/repl* (fig-repl/repl-env* {:open-url "http://localhost:8080/index.html"}) build))

(defrecord FigRepl [host port]
  component/Lifecycle
  (start [component]
    (println "Figwheel REPL" host port)
    (clj-server/start-server
      {:name :figwheel-repl
       :host host
       :port port
       :accept `figwheel-repl}))
  (stop [component]
    (clj-server/stop-server :figwheel-repl)))

(defrecord ClojureRepl [host port]
  component/Lifecycle
  (start [component]
    (println "Clojure REPL" host port)
    (clj-server/start-server
       {:name :clj-repl
        :host host
        :port port
        :accept `clojure.main/repl}))
  (stop [component]
    (clj-server/stop-server :clj-repl)))

(defrecord FigBuild [builds]
  component/Lifecycle
  (start [component]
    (fig-main/start-builds* builds)
    (assoc component :figwheel true))
  (stop [component]
    (dissoc component :figwheel true)))

(defn new-system []
  (component/system-map
    :clj-repl  (map->ClojureRepl {:host "localhost" :port 5554})
    :fig-repl  (map->FigRepl {:host "localhost" :port 5555})
    :fig-build (map->FigBuild {:builds ["dev"]})
    :server    (map->Server {:host "localhost" :port 8080})))
    
(defn -main [& args]
  (component/start (new-system)))
    