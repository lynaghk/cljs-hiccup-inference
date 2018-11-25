(ns sablono.compiler
  "Using this namespace so we can overwrite Rum's default usage of the sablono compiler and use our own."
  (:require hicada.compiler))

(defn compile-html
  [body]
  (hicada.compiler/compile body {:create-element 'js/React.createElement
                                 :rewrite-for? true
                                 :interpret-or-inline-fn (fn [expr]
                                                           (cond
                                                             ;;By convention in my codebase, all react components are prefixed with *, so if we're invoking one of those we know we can inline and don't need to interpret
                                                             (and (list? expr)
                                                                  (.startsWith (name (first expr)) "*"))
                                                             :inline))}))
