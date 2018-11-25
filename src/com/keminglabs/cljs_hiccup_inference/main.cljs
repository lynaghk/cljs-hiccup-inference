(ns com.keminglabs.cljs-hiccup-inference.main
  (:require-macros [com.keminglabs.cljs-hiccup-inference.macros :refer [p pp]])
  (:require [rum.core :as rum]))


(defn two-arity
  ([one]
   (two-arity one 2))

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


   ;;;;;;;;;;;;;;;;;;;
   ;;These should all be automatically inlined thanks to cljs type inference

   (fn-that-returns-string)


   (let [foo 1]
     foo)

   (string-lookup-hinted {:a-string "foo"})

   (pr-str [])

   (str "abc" "...")

   ;;TODO: this should have been resolved by https://dev.clojure.org/jira/browse/CLJS-2901 but doesn't seem to work.
   (two-arity 1)


   ;;;;;;;;;;;;;;;;;
   ;;Other examples

   ;;Have to annotate this, but it might be possible to update Rum to add a type hint to the defined var so that we can automatically inline calls to things we know are react components.
   ^:inline (a-component)

   ;;Inlines because of `:interpret-or-inline-fn` option that chooses based on name
   (*a-prefixed-component)

   ;;this sort of stuff pretty much has to be interpreted at runtime, unless you want to implement abstract interpretation in the ClojureScript compiler
   ^:interpret (vector :div 1 2 3)

   ;;cljs type inference isn't this good yet.
   ^:inline (:a-string {:a-string "foo"})




   ])


(rum/mount (*app) (.getElementById js/document "app"))
