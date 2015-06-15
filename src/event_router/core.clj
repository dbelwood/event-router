(ns event-stream.core
  (:use [clj-kafka.producer])
  (:use [clj-kafka.zk])
  (:gen-class))

(def broker-ids (brokers {"zookeeper.connect" "127.0.0.1:2181"}))

(def p (producer {"metadata.broker.list" "127.0.0.1:9092"
                  "serializer.class" "kafka.serializer.DefaultEncoder"
                  "partitioner.class" "kafka.producer.DefaultPartitioner"}))
(defn -main
  [& args]
  (send-message p (message "test" (.getBytes "this is my message"))))
