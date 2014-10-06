# patchin

Creates and applies patches to nested maps.

## Usage

Add patchin as a dependency to your project:
    `[patchin "0.1.0"]`

```clj
(ns my.namespace
  (:require [patchin :as patchin]))

(def p (patchin/diff {:old {:x 1} {:new {:x 2}))
(patchin/patch {:my "data"} p)
```

## License

Copyright © 2014 Timothy Pratley

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
