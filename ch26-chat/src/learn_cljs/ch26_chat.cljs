(ns ^:figwheel-hooks learn-cljs.ch26-chat
  (:require
   [goog.dom :as gdom]
   [learn-cljs.ch26-chat.message-bus :as bus]
   [learn-cljs.ch26-chat.components.app :refer [init-app]]
   [learn-cljs.ch26-chat.api :as api]
   [learn-cljs.ch26-chat.handlers]
   [learn-cljs.ch26-chat.state :as state]))

(defonce initialized?
         (do
           (api/init! bus/msg-ch js/WS_API_URL)
           (init-app
             (gdom/getElement "app")
             bus/msg-ch)
           (set! (.-getAppState js/window) #(clj->js @state/app-state))
           true))
