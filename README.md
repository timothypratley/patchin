# patchin

Creates and applies patches for nested data structures.
`(= (patch a (diff a b)) b)`

Works with nested maps, sets, sequences and values.

Useful for streaming updates between a client/server to keep a data model
synchronized.

Patches are never larger than the original data structure.

Map/set patches are efficient, avoid sequence patches where possible.


## Usage

Add patchin as a dependency to your project:
    `[patchin "0.1.0"]`

```clj
(ns my.namespace
  (:require [patchin :as patchin]))

(let [p (patchin/diff {:old {:x 1} {:new {:x 2}))]
  (patchin/patch {:my "data"} p))
```


## License

Copyright © 2014 Timothy Pratley

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
