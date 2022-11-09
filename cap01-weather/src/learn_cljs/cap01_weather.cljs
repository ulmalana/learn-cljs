(ns ^:figwheel-hooks learn-cljs.cap01-weather
  (:require
   [goog.dom :as gdom]
   ;;[reagent.core :as reagent :refer [atom]]
   [reagent.dom :as rdom]
   [reagent.core :as r]
   [ajax.core :as ajax]))

;; (println "This text is printed from src/learn_cljs/cap01_weather.cljs. Go ahead and edit it and see reloading in action.")

;; (defn multiply [a b] (* a b))

;; (defn append-element [parent child]
;;   (when-not (.contains parent child)
;;     (.appendChild parent child)))

;; ;; define your app data so that it doesn't get over-written on reload
;; (defonce app-state (atom {:text "Live reloading rocks!"}))

(defonce app-state (r/atom {:title "WhichWeather"
                            :postal-code ""
                            :temperatures {:today {:label "Today"
                                                   :value nil}
                                           :tomorrow {:label "Tomorrow"
                                                      :value nil}}}))
;; (defn get-app-element []
;;   (gdom/getElement "app"))

(def api-key "PUT YOUR API KEY HERE")

(defn handle-response [resp]
  (let [today (get-in resp ["list" 0 "main" "temp"])
        tomorrow (get-in resp ["list" 8 "main" "temp"])]
    (swap! app-state
           update-in [:temperatures :today :value] (constantly today))
    (swap! app-state
           update-in [:temperatures :tomorrow :value] (constantly tomorrow))))

(defn get-forecast! []
  (let [postal-code (:postal-code @app-state)]
    (ajax/GET "http://api.openweathermap.org/data/2.5/forecast"
              {:params {"q" postal-code
                        "appid" api-key
                        "units" "metric"}
               :handler handle-response})))

(defn hello-world []
  [:div
   [:h1 {:class "app-title"} "Hello, Riz"]])

(defn title []
  [:h1 (:title @app-state)])

(defn temperature [temp]
  [:div {:class "temperature"}
   [:div {:class "value"}
    (:value temp)]
   [:h2 (:label temp)]])

(defn postal-code []
  [:div {:class "postal-code"}
   [:h3 "Enter your postal code"]
   [:input {:type "text"
            :placeholder "Postal Code"
            :value (:postal-code @app-state)
            :on-change #(swap! app-state assoc :postal-code (-> % .-target .-value))}]
   [:button {:on-click get-forecast!} "Go"]])

(defn app []
  [:div {:class "app"}
   [title]
   [:div {:class "temperatures"}
    (for [temp (vals (:temperatures @app-state))]
      [temperature temp])]
   [postal-code]])

;; (defn greeter []
;;   [:div
;;    [:h1 "Hello, Riz"]])

;; (defn mount [el]
;;   (rdom/render [greeter] el))

;; (defn mount-app-element []
;;   (when-let [el (get-app-element)]
;;     (mount el)))

(defn mount-app-element []
  (rdom/render [app] (gdom/getElement "app")))

;; conditionally start your application based on the presence of an "app" element
;; this is particularly helpful for testing this ns without launching the app
(mount-app-element)

;; specify reload hook with ^:after-load metadata
(defn ^:after-load on-reload []
  (mount-app-element)
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
