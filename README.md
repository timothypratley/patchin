# patchin

Create, transmit, apply data changes between your browser and server.

* Creates and applies patches for nested data structures.
* Works with nested maps, sets, sequences and values.
* Provided in Clojure and ClojureScript.
* Useful for streaming updates between a client/server to keep a data model
  synchronized. (I recommend Sente for the streaming part).
* Patches are never larger than the original data structure.
* Map/set patches are efficient; avoid sequences where possible.
* Upholds `(= (patch a (diff a b)) b)` as an invariant.


## Usage

Add patchin as a dependency to your project:
    `[timothypratley.patchin "0.2.0"]`

```clj
(ns my.namespace
  (:require [timothypratley.patchin :as patchin]))

(let [p (patchin/diff {:old {:x 1} {:new {:x 2}))]
  (patchin/patch {:my "data"} p))
```

`p` is intended for transmission over a network.


## License

Copyright © 2014 Timothy Pratley

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
