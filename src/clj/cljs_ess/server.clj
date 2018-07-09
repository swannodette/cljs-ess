(ns cljs-ess.server
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]))

(defn respond-hello [request]          
  {:status 200 :body "Hello, world!"})

(def routes
  (route/expand-routes                                   
    #{["/" :get respond-hello :route-name :greet]}))

(defn create-server [host port]
  (http/create-server     
    {::http/routes routes  
     ::http/type   :jetty  
     ::http/port   8890
     ::http/join   false}))
