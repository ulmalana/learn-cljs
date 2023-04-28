(ns learn-cljs.ch26-chat.components.app
  (:require [learn-cljs.ch26-chat.components.header :refer [init-header]]
            [learn-cljs.ch26-chat.components.sidebar :refer [init-sidebar]]
            [learn-cljs.ch26-chat.components.dom :as dom]
            [goog.dom :as gdom])
  (:import [goog.dom TagName]))

(defn init-main []
  (dom/section "content-main"
               (init-header)))

(defn init-app [el msg-ch]
  (let [wrapper (dom/div "app-wrapper"
                         (init-sidebar msg-ch))]
    (set! (.-innerText el) "")
    (.appendChild el wrapper)))


