(ns ^:figwheel-hooks learn-cljs.ch29-notification
  (:require
    [reagent.dom :as rdom]
    [goog.dom :as gdom]
    [learn-cljs.ch29-notification.actor :as actor]
    [learn-cljs.ch29-notifications.command-event :as cmd]
    [learn-cljs.ch29-notifications.pubsub :as pubsub]))

(rdom/render
  ;[pubsub/app]
  ;[cmd/app]
  [actor/app]
  (gdom/getElement "app"))

(enable-console-print!)
