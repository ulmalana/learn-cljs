(ns ^:figwheel-hooks learn-cljs.ch10-choices
  (:require
   [goog.dom :as gdom]
   [bterm.core :as bterm]
   [bterm.io :as io]
   [learn-cljs.data :as data]))

(def term (bterm/attach (gdom/getElement "app")
                        {:prompt "=> "
                         :font-size 14}))

(declare on-answer)

(defn prompt [game current]
  (let [scene (get game current)
        type (:type scene)]
    (io/clear term)
    (when (or (= :win type)
              (= :lose type))
      (io/print term
                (if (= :win type)
                  "You have won "
                  "Game over ")))
    (io/println term (:title scene))
    (io/println term (:dialog scene))
    (io/read term #(on-answer game current %))))

(defn on-answer [game current answer]
  (let [scene (get game current)]
    (if (= :skip (:type scene))
      (:on-continue scene)
      (condp = answer
        "reset" (prompt game :start)
        "help" (do (io/clear term)
                   (io/println term "Valid commands:")
                   (io/println term "\t- %s: %s" "reset" "Return to the beginning of the game")
                   (io/println term "\t- %s: %s" "help" "Display help message")
                   (io/println term "\t- %s: %s" "yes" "Answer in the affirmative")
                   (io/println term "\t- %s: %s" "no" "Answer in the negative")
                   (io/println term "Press enter to continue.")
                   (io/read term #(prompt game current)))
        "yes" (prompt game (get-in scene [:transitions "yes"]))
        "no" (prompt game (get-in scene [:transitions "no"]))
        (do (io/clear term)
            (io/println term "I didnt understand that. You can enter \"help\" for help")
            (io/println term "Press enter to continue")
            (io/read term #(prompt game current)))))))


(prompt data/game :start)

;; define your app data so that it doesn't get over-written on reload
(defonce app-state (atom {:text "Hello world!"}))

(defn get-app-element []
  (gdom/getElement "app"))



;; specify reload hook with ^:after-load metadata
(defn ^:after-load on-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
