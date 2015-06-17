(ns event-router.core
  (:require [event-router.consumer :as consumer])
  (:require [event-router.producer :as producer])
  (:require [event-router.utils :as utils])
  (:require [clojure.data.json :as json])
  (:gen-class))

;; Configuration
(def zookeeper-address "127.0.0.1:2181")

(def c (consumer/consumer zookeeper-address "event-router"))
(def p (producer/producer (utils/broker-addresses zookeeper-address)))

(defn -main
  [& args]
  "Publish and consume a message"
  (let [topics ["source"]
        iter (take 100 (cycle ["a" "b"]))
        messages (map #(json/write-str {:key %1 :value "test"}) iter)]
        (utils/create-topics zookeeper-address topics)
        (doseq [msg messages]
          (producer/publish-message p (first topics) msg))
        (println (map
                  #(String. (.value %1))
                  (take 100 (consumer/get-topic-stream c (first topics)))))))
