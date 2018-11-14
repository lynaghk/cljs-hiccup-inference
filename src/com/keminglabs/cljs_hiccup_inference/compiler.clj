(ns com.keminglabs.cljs-hiccup-inference.compiler
  (:require hicada.compiler
            [clojure.pprint :refer [pprint]]
            cljs.analyzer))


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


;; (defmacro defc
;;   [& args]
;;   `(rum.core/defc
;;      ~@(butlast args)
;;      ~(compile-html-env (last args) &env)))

;; (defmacro defcs
;;   [& args]
;;   `(~'rum.core/defcs
;;     ~@(butlast args)
;;     ~(compile-html-env (last args) &env)))


;; (defmacro foo
;;   [x]
;;   (binding [*out* *err*]
;;     (pprint (cljs.analyzer/analyze &env x))))


;; (cljs.analyzer/analyze {} '[:div

;;                             (let [foo "foo"]
;;                               [:h1 foo])

;;                             (let [foo 1]
;;                               [:h2 foo])

;;                             ])


;; (macroexpand-1 '(defc *foo
;;                   []
;;                   (let [foo (fn [] "sooo")]
;;                     [:div (foo)])))


(comment
  (macroexpand '(html [:div ^:interpret (unknown-result)]))
  (macroexpand '(html [:div ^:inline (unknown-result)]))
  (macroexpand '(html [:div (*foo 1 2 3)]))
  (macroexpand '(html [:div (foo 1 2 3)]))

  (macroexpand '(html [:div (for [x (range 5)
                                  y (range 3)
                                  :where (even? x)]
                              [:div x y])]))

  (macroexpand '(html [:div 1]))


  (macroexpand '(html (list [:div.a]
                            [:.b])))

  (compile-html [:div [:h1 "Hello"]])

  (compile-html [:div (for [x (range 5)]
                        [:h1 x])])

  (html '[:div (for [x (range 5)]
                 [:h1 x])])




  )
