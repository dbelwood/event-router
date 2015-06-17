(ns event-router.utils
  (:require [clj-kafka.zk :as zk])
  (:require [clj-kafka.admin :as admin])
  (:require [clojure.string :as str]))

(defn zookeeper-connect [address] {"zookeeper.connect" address})

(defn broker-addresses [zookeeper-address]
  "Get kafka broker addresses from a running zk instance"
  (zk/broker-list (zk/brokers (zookeeper-connect zookeeper-address))))

(def topic-does-not-exist? (complement admin/topic-exists?))

(defn create-topics [zookeeper-address topics]
  "Given a list of topics, create the ones that don't exist
   and return the created topics"
  (let [zk (admin/zk-client zookeeper-address)
        exists? (partial topic-does-not-exist? zk)
        topics-to-create (filter exists? topics)
        create (partial admin/create-topic zk)]
    (if (every? true? (map create topics-to-create)) :ok :error)))
