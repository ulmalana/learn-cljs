(ns ^:figwheel-hooks learn-cljs.ch22-state
  (:require
   [goog.dom :as gdom]
   [goog.events :as gevents]
   [hiccups.runtime])
  (:require-macros [hiccups.core :as hiccups]))

(def counter (atom 0))

(def creature
  (atom {:type "water"
         :life 50
         :abilities ["swimming" "speed"]}))

(def base-creature @creature)

(set-validator! creature
                (fn [x] (>= (:life x) 0)))

(defonce app-state (atom 0))

(def app-container (gdom/getElement "app"))

(defn render [state]
  (set! (.-innerHTML app-container)
        (hiccups/html
          [:div
           [:p "Counter: " [:strong state]]
           [:button {:id "up"} "+"]
           [:button {:id "down"} "-"]])))

(defonce is-initialized?
         (do
           (gevents/listen (gdom/getElement "app") "click"
                           (fn [e]
                             (condp = (aget e "target" "id")
                               "up" (swap! app-state inc)
                               "down" (swap! app-state dec))))
           (add-watch app-state :counter-observer
                      (fn [key atom old-val new-val]
                        (render new-val)))
           (render @app-state)
           true))



;; specify reload hook with ^:after-load metadata
(defn ^:after-load on-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
