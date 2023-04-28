(ns learn-cljs.ch26-chat.components.component
  (:require [learn-cljs.ch26-chat.state :as state]))

(defn init-component
  "Initialize a component.
  Params:
  el - element in which to render component
  watch-key - key that uniquely identifies this component
  accessor - function that takes the app state and returns the component state
  render - function that takes the parent element and component state and renders dom"
  [el watch-key accessor render]
  (add-watch state/app-state watch-key
             (fn [_ _ old new]
               (let [state-old (accessor old)
                     state-new (accessor new)]
                 (when (not= state-old state-new)
                   (set! (.-innerText el) "")
                   (render el state-new)))))
  (render el (accessor @state/app-state))
  el)
