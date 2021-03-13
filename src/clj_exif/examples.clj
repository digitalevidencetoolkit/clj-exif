(ns clj-exif.examples
  "Examples usage of the exif utilities."
  (:require [clojure.java.io :as jio]
            [clojure.pprint :as pprint]
            [clojure.set :as set]
            [clj-exif.core :as exif])
  (:import
    [java.io File]
    [org.apache.commons.imaging.formats.tiff.constants
     TiffDirectoryConstants
     TiffTagConstants
     GpsTagConstants]
    [org.apache.commons.imaging.common RationalNumber]))

;; read data
(let [input-file (jio/as-file (jio/resource "gps.JPG"))
      metadata (exif/get-metadata input-file)]
  (println (pprint/pprint (exif/read metadata))))

(let [input-file (File. "/tmp/output.jpg")
      metadata (exif/get-metadata input-file)]
  (println (pprint/pprint (exif/read metadata))))

;; update the make of the device
(let [input-file (jio/as-file (jio/resource "pic.geo.jpg"))
      output-file (File. "/tmp/output.jpg")
      metadata (exif/get-metadata input-file)
      ;; output-set is a writeable copy of the data retrieved from input-file.
      output-set (exif/get-output-set metadata)]
  (exif/update-value output-set
                     TiffDirectoryConstants/DIRECTORY_TYPE_ROOT
                     TiffTagConstants/TIFF_TAG_MAKE
                     ["this is a new value"])
  (exif/copy-file-with-new-metadata input-file output-file output-set))

(let [input-file   (jio/as-file (jio/resource "gps.JPG"))
      output-file  (jio/as-file (jio/resource "out.jpg"))
      metadata     (exif/get-metadata input-file)
      output-set  (exif/get-output-set metadata)]
  (exif/update-value output-set
                     TiffDirectoryConstants/DIRECTORY_TYPE_GPS
                     GpsTagConstants/GPS_TAG_GPS_LATITUDE
                     [(RationalNumber. 46 1)
                      (RationalNumber. 22 1)
                      (RationalNumber. 562 10)])
  (exif/update-value output-set
                     TiffDirectoryConstants/DIRECTORY_TYPE_GPS
                     GpsTagConstants/GPS_TAG_GPS_LONGITUDE
                     [(RationalNumber. 6 1)
                      (RationalNumber. 14 1)
                      (RationalNumber. 334 10)])
  (exif/update-value output-set
                     TiffDirectoryConstants/DIRECTORY_TYPE_ROOT
                     TiffTagConstants/TIFF_TAG_MAKE
                     ["Huawei"])
  (exif/update-value output-set
                     TiffDirectoryConstants/DIRECTORY_TYPE_ROOT
                     TiffTagConstants/TIFF_TAG_MODEL
                     ["P30 Pro back camera"])
  (exif/copy-file-with-new-metadata input-file output-file output-set))
