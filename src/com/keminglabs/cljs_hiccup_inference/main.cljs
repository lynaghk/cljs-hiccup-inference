(ns com.keminglabs.cljs-hiccup-inference.main
  (:require-macros [com.keminglabs.cljs-hiccup-inference.compiler :as c]
                   [com.keminglabs.cljs-hiccup-inference.macros :refer [p pp]])
  (:require [rum.core :as rum]
            hicada.interpreter))


(c/defc *app
  []
  [:h1 (list "hello" " world")])

(rum/mount (*app) (.getElementById js/document "app"))
