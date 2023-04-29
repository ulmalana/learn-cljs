(ns learn-cljs.ch26-chat.handlers
  (:require [learn-cljs.ch26-chat.message-bus :as bus]
            [learn-cljs.ch26-chat.api :as api]
            [learn-cljs.ch26-chat.state :as state]))

(defn should-set-message? [username room]
  (let [app @state/app-state]
    (or
      (and (some? username)
           (state/is-current-view-conversations? app)
           (= username (state/current-conversation-recipient app)))
      (and (some? room)
           (state/is-current-view-room? app)
           (= room (state/current-room-id app))))))

(defonce is-initialized?
         (do
           (bus/handle! bus/msg-bus :switch-to-conversation
                        (fn [{:keys [username]}]
                          (api/send! :set-view {:type :conversation
                                                :username username})
                          (swap! state/app-state
                                 #(-> %
                                      (state/switched-to-conversation username)
                                      (state/messages-cleared)))))

           (bus/handle! bus/msg-bus :switch-to-room
                        (fn [{:keys [id]}]
                          (api/send! :set-view {:type :room :id id})
                          (swap! state/app-state
                                 #(-> %
                                      (state/switched-to-room id)
                                      (state/messages-cleared)))))

           (bus/handle! bus/msg-bus :add-message
                        (fn [content]
                          (api/send! :add-message {:content content})))

           (bus/handle! bus/msg-bus :api/people-listed
                        (fn [people]
                          (swap! state/app-state state/received-people-list people)))

           (bus/handle! bus/msg-bus :api/person-joined
                        (fn [person]
                          (swap! state/app-state state/person-joined person)))

           (bus/handle! bus/msg-bus :api/person-left
                        (fn [username]
                          (swap! state/app-state state/person-left username)))

           (bus/handle! bus/msg-bus :open-create-room-input
                        (fn []
                          (swap! state/app-state state/create-room-input-opened)))

           (bus/handle! bus/msg-bus :close-create-room-input
                        (fn []
                          (swap! state/app-state state/create-room-input-closed)))

           (bus/handle! bus/msg-bus :create-room
                        (fn [name]
                          (api/send! :create-room {:name name})))

           (bus/handle! bus/msg-bus :api/rooms-listed
                        (fn [rooms]
                          (swap! state/app-state state/received-rooms-list rooms)
                          (when-let [first-room (first rooms)]
                            (bus/dispatch! bus/msg-bus :switch-to-room
                                           {:id (:id first-room)}))))

           (bus/handle! bus/msg-bus :api/room-created
                        (fn [room]
                          (swap! state/app-state
                                 #(-> %
                                      (state/room-added room)
                                      (state/create-room-input-closed)))))

           (bus/handle! bus/msg-bus :api/message-received
                        (fn [{:keys [message room username]}]
                          (when (should-set-message? username room)
                           (swap! state/app-state state/message-received message))))

           (bus/handle! bus/msg-bus :api/messages-received
                        (fn [{:keys [messages room username]}]
                          (when (should-set-message? username room)
                            (swap! state/app-state state/messages-received messages))))

           (bus/handle! bus/msg-bus :toggle-auth-modal
                        (fn []
                          (swap! state/app-state state/auth-modal-toggled)))

           (bus/handle! bus/msg-bus :sign-in
                        (fn [data]
                          (api/send! :sign-in data)))

           (bus/handle! bus/msg-bus :sign-up
                        (fn [data]
                          (api/send! :sign-up data)))

           (bus/handle! bus/msg-bus :api/authenticated
                        (fn [user-info]
                          (swap! state/app-state state/user-authenticated user-info)
                          (api/send! :list-people)
                          (api/send! :list-rooms)))
           true))
