(ns com.keminglabs.cljs-hiccup-inference.compiler
  (:require hicada.compiler
            cljs.analyzer))

;;https://gist.github.com/mfikes/3837f510aad3e8ff766eb9071ef66b2b
(defn infer-type
  [form env]
  (cljs.analyzer/infer-tag env
                           (cljs.analyzer/no-warn (cljs.analyzer/analyze env form))))

(defmethod hicada.compiler/compile-form "list"
  [[_ & forms]]
  `(cljs.core/array ~@(mapv hicada.compiler/emitter forms)))


(defmethod hicada.compiler/compile-form :default
  [expr]
  (let [{:keys [interpret inline]} (meta expr)]
    (cond
      inline
      expr

      ;;by convention, calls like (*foo) return react elements
      (and (list? expr)
           (.startsWith (name (first expr)) "*"))
      expr

      interpret
      `(hicada.interpreter/interpret ~expr)

      (symbol? expr) ;;don't worry about vars
      expr

      :else
      (binding [*out* *err*]
        ;;(prn "Type: " (infer-type expr hicada.compiler/*env*))
        (println "WARNING: interpreting form by default; Specify ^:inline or ^:interpret" (when-let [line (:line (meta expr))]
                                                                                            (str "line " line)))
        (prn (meta expr))
        (prn expr) ;;TODO: print original source code.
        `(hicada.interpreter/interpret ~expr)))))


(defn compile-html-env
  [body env]
  (hicada.compiler/compile body {:create-element 'js/React.createElement
                                 :rewrite-for? true}
                           {}
                           env))

(defn html
  [body]
  (hicada.compiler/compile body {:create-element 'js/React.createElement
                                 :rewrite-for? true}
                           {}))

(defmacro defc
  [& args]
  `(rum.core/defc
     ~@(butlast args)
     ~(compile-html-env (last args) &env)))

(defmacro defcs
  [& args]
  `(~'rum.core/defcs
    ~@(butlast args)
    ~(compile-html-env (last args) &env)))


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
