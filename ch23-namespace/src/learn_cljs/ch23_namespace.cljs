(ns ^:figwheel-hooks learn-cljs.ch23-namespace
  (:require
   [goog.dom :refer [getElement]
    :rename {getElement get-element}]
   [learn-cljs.ch23-namespace.ui]
   [learn-cljs.ch23-namespace.format :refer [pluralize]]
   [learn-cljs.ch23-namespace.inventory :as inventory]))

(defn item-description [i item]
  (let [qty (inventory/item-qty i item)
        label (if (> qty 1)
                (pluralize item)
                item)]
    (str qty " " label)))

(let [i (-> (inventory/make-inventory)
            (inventory/add-items "Laser Catapult" 1)
            (inventory/add-items "Antimatter Scrubber" 5))]
  (learn-cljs.ch23-namespace.ui/render-list (get-element "app")
                                            (map (partial item-description i)
                                                 (inventory/list-items i))))
