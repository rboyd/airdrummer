airdrummer
==========

Turning your webcam into a musical instrument with a little help from OpenCV and Overtone.

## Installation

You'll need to install opencv (brew install opencv) and pull down
nakkaya's [vision](https://github.com/nakkaya/vision) clojure library.
Get that built, and drop the dylib in airdrummer's root dir.


## Usage

Invoke airdrummer.core/start and wait for the camera. Movement will be
identified and trigger audio via Overtone.

![Air Drums](https://raw.github.com/wiki/rboyd/airdrummer/images/chops.png)

