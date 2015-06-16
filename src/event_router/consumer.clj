(ns event-router.consumer
  (:require [clj-kafka.consumer.zk :as zk])
  (:require [clj-kafka.core :as core]))

(defn consume-message [consumer-config topic]
  "Consume 1 message from a topic"
  (core/with-resource [c (zk/consumer consumer-config)]
    zk/shutdown
    (first (zk/messages c topic))))

(defn get-topic-stream [consumer-config topic]
  "Continually read messages for a topic"
    (let [c (zk/consumer consumer-config)]
      (zk/stream-seq (zk/create-message-stream c topic))))
