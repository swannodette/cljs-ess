(ns cljs-ess.system
  (:require [cljs-ess.server :as server]
            [com.stuartsierra.component :as component]))

(defn -main [& args]
  (server/start))
