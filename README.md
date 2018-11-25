# Fast ClojureScript React templates

This is a minimal example repo demonstrating a cljs hiccup compiler that:

+ warns about runtime interpretation, which nudges you to write faster code
+ uses type inference to avoid interpretation of values that can be passed directly to React

For background, see: https://kevinlynagh.com/notes/fast-cljs-react-templates/


## Run

You'll need [Clojure CLI](https://clojure.org/guides/getting_started).
Then run:

    clj -A:figwheel
    
And Figwheel will open a live-reloading page in your default browser.

Test templates are defined in src/com/keminglabs/cljs_hiccup_inference/main.cljs
