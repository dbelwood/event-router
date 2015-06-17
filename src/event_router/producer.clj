(ns event-router.producer
  (:require [clj-kafka.producer :as p])
  (:require [clj-kafka.zk :as zk]))

(defn- generate-producer-config [broker-addresses]
  {"metadata.broker.list" broker-addresses
   "serializer.class" "kafka.serializer.StringEncoder"
   "partitioner.class" "kafka.producer.DefaultPartitioner"})

(defn producer [broker-addresses]
  (p/producer (generate-producer-config broker-addresses)))

(defn publish-message [prod topic msg]
  "Publish a message on a topic"
  (p/send-message prod (p/message topic msg)))

