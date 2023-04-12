(ns ^:figwheel-hooks learn-cljs.ch17-shopping-cart
  (:require
   [goog.dom :as gdom]))

(def tax-rate 0.079)

(def cart [{:name "Silicone Pancake Mold" :price 10.49 :taxable? false}
           {:name "Small Pourover Coffe Maker" :price 18.96 :taxable? true}
           {:name "Digital Kitchen Scale" :price 24.95 :taxable? true}])

(defn add-sales-tax [cart-item]
  (assoc cart-item :sales-tax (* (:price cart-item) tax-rate)))

(def taxable-cart
  (map add-sales-tax (filter :taxable? cart)))

(def item-list (gdom/createDom "ul" nil ""))

(defn display-item [item]
  (str (:name item)
       ": "
       (:price item)
       " (tax: "
       (.toFixed (:sales-tax item) 2)
       ")"))

(doseq [item taxable-cart]
  (gdom/appendChild
    item-list
    (gdom/createDom "li" #js {} (display-item item))))

(gdom/removeChildren (.-body js/document))
(gdom/appendChild (.-body js/document) item-list)
