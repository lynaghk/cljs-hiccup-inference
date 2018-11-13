(ns com.keminglabs.cljs-hiccup-inference.macros)

(defmacro p
  "Print and return native JavaScript argument."
  [x]
  `(let [res# ~x]
     (~@(if (:ns &env) '[.log js/console] '[prn]) res#)
     res#))

(defmacro pp
  "Pretty print and return argument (uses `prn-str` internally)."
  [x]
  `(let [res# ~x]
     (~@(if (:ns &env) '[.log js/console] '[prn]) (prn-str res#))
     res#))
