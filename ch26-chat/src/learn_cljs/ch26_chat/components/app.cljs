(ns learn-cljs.ch26-chat.components.app
  (:require [learn-cljs.ch26-chat.components.header :refer [init-header]]
            [learn-cljs.ch26-chat.components.sidebar :refer [init-sidebar]]
            [learn-cljs.ch26-chat.components.dom :as dom]
            [learn-cljs.ch26-chat.components.messages :refer [init-messages]]
            [learn-cljs.ch26-chat.components.compose :refer [init-composer]]
            [learn-cljs.ch26-chat.components.auth :refer [init-auth]]))

(defn init-main [msg-ch]
  (dom/section "content-main"
               (init-header)
               (init-messages)
               (init-composer msg-ch)))

(defn init-app [el msg-ch]
  (let [wrapper (dom/div "app-wrapper"
                         (init-sidebar msg-ch)
                         (init-main msg-ch)
                         (init-auth msg-ch))]
    (set! (.-innerText el) "")
    (.appendChild el wrapper)))


