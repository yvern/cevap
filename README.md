# Cevap

Not only delicious food, this is also a simple example on how to use/deploy/dockerize  [neanderthal](https://neanderthal.uncomplicate.org) based applications, using installation instructions as seen on this [gist](https://gist.github.com/pachamaltese/afc4faef2f191b533556f261a46b3aa8).

I could not find any example on how to do it, so here it is!

## Do try this at home!

- `docker build -t cevap .` beware: this may take a long time, as mkl installs a lot of libs, go grap a coffee
- `docker run --name cevap --rm -d -p 3000:3000 cevap`
- `curl localhost:3000/rec?max=100`
- `docker kill cevap`

## License

Copyright © 2019 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
