(ns cljs-ess.system
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [figwheel.repl :as fig-repl]
            [figwheel.core :as fig-core]
            [cljs.repl :as repl]
            [clojure.core.server :as clj-server]
            [io.pedestal.http :as http]
            [cljs-ess.server :as server]
            [com.stuartsierra.component :as component]))

(defrecord Server [host port]
  component/Lifecycle
  (start [component]
    (println "Start webserver")
    (let [server (server/create-server host port)]
      (http/start server)
      (assoc component :server server)))
  (stop [{:keys [server] :as component}]
    (http/stop server)
    (dissoc component :server)))

(defn figwheel-repl []
  (let [build (-> "dev.cljs.edn" io/file slurp edn/read-string)]
    (repl/repl* (fig-repl/repl-env* {}) build)))

(defrecord FigRepl [host port]
  component/Lifecycle
  (start [component]
    (println "FigRepl" host port)
    (clj-server/start-server
      {:name :figwheel-repl
       :host host
       :port port
       :accept `figwheel-repl}))
  (stop [component]
    (clj-server/stop-server :figwheel-repl)))

(defrecord FigBuild [])

(defn new-system []
  (component/system-map
    :repl   (map->FigRepl {:host "localhost" :port 5555})
    :server (map->Server {:host "localhost" :port 8080})))
    
(defn -main [& args]
  (component/start (new-system)))
