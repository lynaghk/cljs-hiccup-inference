(ns com.keminglabs.cljs-hiccup-inference.main
  (:require-macros [com.keminglabs.cljs-hiccup-inference.macros :refer [p pp]])
  (:require [rum.core :as rum]))


(defn two-arity
  "Ignore this browser event? Used so that things don't happen when people are typing in an <input> or whatever."
  ([one] (two-arity one 2))

  ([one two]
   (str one "-" two)))

(defn fn-that-returns-string
  []
  "Fn that returns string")


(rum/defc a-component
  []
  [:span "Some react component"])


(rum/defc *a-prefixed-component
  []
  [:span "Some react component"])


;;NOTE: unlike in JVM Clojure, hint is lowercase and comes before var.
(defn ^string string-lookup-hinted
  [x]
  (:a-string x))


(rum/defc *app
  []
  [:div

   ;;should be inferred safe to inline
   (fn-that-returns-string)

   ;;TODO: this is safe to inline (it's another react component); can we propagate this via type inference, or does there need to be another mechanism?
   (a-component)

   ;;this should inline because of my `:interpret-or-inline-fn` option that chooses based on name
   (*a-prefixed-component)

   ;;this must be interpreted; should emit warning
   (vector :div 1 2 3)

   ;;with metadata, shouldn't emit warning
   ^:interpret (vector :div 1 2 3)

   ;;local safe to inline
   (let [foo 1]
     foo)

   (string-lookup-hinted {:a-string "foo"})


   ;;;;;;;;;;;;;;;;;;;;
   ;;TODO cljs type inference incomplete?
   ;;These should all be knowable

   (pr-str [])

   (:a-string {:a-string "foo"})

   (str "abc" "...")
   
   (two-arity 1)
   ])


(rum/mount (*app) (.getElementById js/document "app"))
