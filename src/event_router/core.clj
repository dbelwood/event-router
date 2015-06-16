(ns event-router.core
  (:use [clj-kafka.producer])
  (:use [clj-kafka.zk])
  (:use [clj-kafka.core])
  (:use [clj-kafka.admin])
  (:require [clojure.string :as str])
  (:require [event-router.consumer :as consumer])
  (:gen-class))

;; Configuration
(def zookeeper-connect {"zookeeper.connect" "127.0.0.1:2181"})
(def broker-addresses (str/join ", " (
                       map
                       (partial str/join ":")
                       (map
                        #(map %1 [:host :port])
                        (into () (brokers zookeeper-connect))))))
(def producer-config {"metadata.broker.list" broker-addresses
                      "serializer.class" "kafka.serializer.DefaultEncoder"
                      "partitioner.class" "kafka.producer.DefaultPartitioner"})

(def consumer-config (conj zookeeper-connect {"group.id" "event-router"
                                              "auto.offset.reset" "smallest"
                                              "auto.commit.enable" "false"}))
(def p (producer producer-config))


(defn- publish-message
  [topic msg]
  "Publish a message on a topic"
  (let [message-bytes (.getBytes msg)]
  (send-message p (message topic message-bytes))))

(def topic-does-not-exist? (complement topic-exists?))

(defn- create-topics
  [topics]
  "Given a list of topics, create the ones that don't exist and return the created topics"
  (let [zk (zk-client (get zookeeper-connect "zookeeper.connect"))
        exists? (partial topic-does-not-exist? zk)
        topics-to-create (filter exists? topics)
        create (partial create-topic zk)]
    (if (every? true? (map create topics-to-create)) :ok :error)))

(defn -main
  [& args]
  "Publish and consume a message"
  (let [topics ["source"]]
        (create-topics topics)
        (publish-message "source" "test")))
        ;;(consumer/consume-message "source")))
