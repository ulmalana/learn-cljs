(ns ^:figwheel-hooks learn-cljs.ch26-chat
  (:require
   [goog.dom :as gdom]
   [learn-cljs.ch26-chat.message-bus :as bus]
   [learn-cljs.ch26-chat.components.app :refer [init-app]]
   [learn-cljs.ch26-chat.handlers]))

(defonce initialized?
         (do
           (init-app
             (gdom/getElement "app")
             bus/msg-ch)
           true))
