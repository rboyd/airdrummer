(ns airdrummer.core
  (:require [overtone.live :as overtone]
            [vision.core :as vision]))


(def bass (overtone/sample (overtone.live/freesound-path 26888)))
(def snare (overtone/sample (overtone.live/freesound-path 3154)))

(overtone/definst hat [volume 1.0]
  (let [src (overtone/white-noise)
        env (overtone/env-gen (overtone/perc 0.001 0.3) :action overtone/FREE)]
    (* volume 1 src env)))

(let [state (ref {:run true})]
  (defn start []
    (let [capture (vision/capture-from-cam 0)]
      (Thread/sleep 2000)
      (dosync (alter state assoc
                     :run true
                     :prev (vision/clone-image (vision/query-frame capture))))
      (future
       (while (:run @state)
         (let [curr (vision/query-frame capture)
               prev (:prev @state)
               processed (vision/--> (vision/abs-diff curr prev)
                              (vision/convert-color :bgr-gray)
                              (vision/smooth :gaussian 9 9 0 0)
                              (vision/threshold 30 255 :binary))
               rects (vision/with-contours [c [processed :external :chain-approx-none [0 0]]]
                       (vision/bounding-rects c))
               display (vision/clone-image curr)]

           (dosync (alter state assoc :prev (vision/clone-image curr)))

           (if (> (count rects) 0) (bass))
           (doseq [[x y w h] rects]
             (vision/rectangle display [x y] [(+ w x) (+ h y)] java.awt.Color/red 5))

           (vision/view :motion display)

           (vision/release [prev processed display])))
       (vision/release capture))))

  (defn fin []
    (dosync (alter state assoc :run false))))
