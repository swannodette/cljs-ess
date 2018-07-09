(ns cljs-ess.system
  (:require [io.pedestal.http :as http]
            [cljs-ess.server :as server]
            [com.stuartsierra.component :as component]))

(defrecord Server [host port]
  component/Lifecycle
  (start [component]
    (let [server (server/create-server host port)]
      (http/start server)
      (assoc component :server server)))
  (stop [{:keys [server] :as component}]
    (http/stop server)
    (dissoc component :server)))

(defn new-system []
  (component/system-map 
    :server (map->Server {:host "localhost" :port 8890})))

(defn -main [& args]
  (component/start (new-system)))
