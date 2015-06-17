(ns event-router.consumer
  (:require [clj-kafka.consumer.zk :as zk])
  (:require [clj-kafka.core :as core])
  (:require [event-router.utils :as utils]))

(defn consumer [zookeeper-address group-id]
  (let [config (conj (utils/zookeeper-connect zookeeper-address)
                     {"group.id" group-id}
                     {"auto.offset.reset" "smallest"
                      "auto.commit.enable" "false"})]
    (zk/consumer config)))


(defn consume-message [consumer-config topic]
  "Consume 1 message from a topic"
  (core/with-resource [c (zk/consumer consumer-config)]
    zk/shutdown
    (first (zk/messages c topic))))

(defn get-topic-stream [c topic]
  "Continually read messages for a topic"
    (zk/stream-seq (zk/create-message-stream c topic)))
