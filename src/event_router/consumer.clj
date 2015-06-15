(ns event-stream.core
  (:require [clj-kafka.producer :as kp]
            [clj-kafka.zk :as kzk]
            [clj-kafka.consumer.simple :as kc]
            [clj-kafka.core :as k]
            [zookeeper :as zk])
  (:import [clj_kafka.core KafkaMessage]))

  (defn- read-loop

    )

  (defn start-consumer
    "Start a consumer for a specific group/topic"
    [topic partition group-id]
    (let [kpartition (Integer/parseInt partition)
          commit-interval (:commit-interval defaults)
          chan-capacity (:chan-capacity defaults)
          client-id "event-stream-consumer"
          m {"zookeeper.connect" (:zookeeper defaults)}
          partitions (kzk/partitions m topic)
          ch (chan chan-capacity)
          pending-messages (atom {})
          pending-commits (atom (sorted-set))
          reader-fut (future (read-loop m client-id group-id topic partitions kpartition task-map ch pending-commits))]
      {:channel ch
       :reader-future reader-fut
       :pending-messages pending-messages
       :pending-commits pending-commits
       :drained (atom false)}))

  // Shutdown consumer
)
