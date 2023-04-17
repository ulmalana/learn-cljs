(ns ^:figwheel-hooks learn-cljs.ch21-fp
  (:require
   [goog.dom :as gdom]
   [clojure.string :as s]))


(def alan-p {:first-name "Alan"
             :last-name "Perlis"
             :online? false})

(defn nickname [entity]
  (or (:nickname entity)
      (->> entity
           ((juxt :first-name :last-name))
           (s/join " "))))

(defn bold [child]
  [:strong child])

(defn concat-strings [s1 s2]
  (s/trim (str s1 " " s2)))

(defn with-class [dom class-name]
  (if (map? (second dom))
    (update-in dom [1 :class] concat-strings class-name)
    (let [[tag & children] dom]
      (vec (concat [tag {:class class-name}]
                   children)))))

(defn with-status [dom entity]
  (with-class dom
    (if (:online? entity) "online" "offline")))

(defn user-status [user]
  [:div {:class "user-status"}
   ((juxt (comp bold nickname)
          (partial with-status
                   [:span {:class "status-indicator"}]))
    user)])

(defn get-current-hour []
  (.getHours (js/Date.)))

(defn get-time-of-day-greeting [hour]
  (condp >= hour
    11 "Good morning"
    15 "Good day"
    "Good evening"))

(def blog {:title "Functional ClojureScript"
           :tags ["ClojureScript" "FP"]
           :rating 4})

(def new-blog
  (-> blog
      (update-in [:tags] conj "immutability")
      (update-in [:rating] inc)
      (update-in [:title] #(str % " for fun and profit"))
      (assoc :new? true)))

(defn make-mailbox
  ([] (make-mailbox {:messages []
                     :next-id 1}))
  ([state]
   {:deliver!
    (fn [msg]
      (make-mailbox
       (-> state
           (update :messages conj
                   (assoc msg :read? false :id (:next-id state)))
           (update :next-id inc))))
    :next-unread
    (fn []
      (when-let [msg (->> (:messages state)
                          (filter (comp not :read?))
                          (first))]
        (dissoc msg :read?)))
    :read!
    (fn [id]
      (make-mailbox
       (update state :messages
               (fn [messages]
                 (map #(if (= id (:id %)) (assoc % :read? true) %)
                      messages)))))}))

(defn call [obj method & args]
  (apply (get obj method) args))

(defn test-mailbox []
  (loop [mbox (-> (make-mailbox)
                  (call :deliver! {:subject "Objects are cool"})
                  (call :deliver! {:subject "Closures rule"}))]
    (when-let [next-message (call mbox :next-unread)]
      (println "Got message" next-message)
      (recur
       (call mbox :read! (:id next-message)))))
  (println "Read all messages."))

(defn handler [req]
  (println "calling API with" req)
  {:data "is fake"})

(defn validate-request [req]
  (cond
    (nil? (:id req)) {:error "id must be present"}
    (nil? (:count req)) {:error "count must be present"}
    (< (:count req) 1) {:error "count must be positive"}))


(defn with-validation [handler]
  (fn [req]
    (if-let [error (validate-request req)]
      error
      (handler req))))

(defn with-logging [handler]
  (fn [req]
    (println "Request" req)
    (let [res (handler req)]
      (println "response" res)
      res)))

(comment
  (let [wrap-handler (comp with-logging with-validation)
        handler (wrap-handler handler)]
    (handler {})
    (handler {:id 123 :count 12})))


(defn get-app-element []
  (gdom/getElement "app"))



;; specify reload hook with ^:after-load metadata
(defn ^:after-load on-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
