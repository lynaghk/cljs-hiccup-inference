(ns sablono.compiler
  "Using this namespace so we can overwrite Rum's default usage of the sablono compiler and use our own."
  (:require [com.keminglabs.cljs-hiccup-inference.compiler :as c]))

(defn compile-html
  [body]
  (c/compile-html body))
