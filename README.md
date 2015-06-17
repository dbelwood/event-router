# Event Router
## Route/transform/multicast events from service service using Clojure and Kafka

## Events
Events are immutable data emitted by services and event routers.  Much like data structures in Clojure, you can produce new and changed copies of the original event, but the event must be guaranteed to not change over time.

### Types

- Is type a difference without distinction?

* Light Weight Events - published by services as they process a specific transaction, i.e. Shipping Events or Fulfillment Events
* Completion Events - published by services when their transaction is complete and they wish to deliver their finished work

## Publishing Events

* Polyglot publication of events

### Publishing operations

* `(publish-events events)`

## Processing Events

### Event Routers

* Can filter/transform incoming data and create 1-n new copies of the event and publish to 1-m topics
* Should log their processing metrics to a monitoring topic(s)

#### Router operations

* Validation Function - validates incoming events for correctness
* Transformation Function - turn incoming event data structure into 0-n new data structures
* HandleInvalidEventFunction - put event on bad event topic for this router and do any cleaning necessary
*  - reset offset to re-process incoming message

### Event Consumers

#### Common operations

* `consumeEvent` - pull down the next event

#### Services

* High-level consumer that does what ever it wants to the event

#### Reporting Service

* Capture the event for sending via the reporting service

#### Monitoring

* Send to time-series data store
* Event flow visualization?

## Handling Errors

* Events that cause errors should go to error queues defined by a consumer.  Ad hoc transformers should be able to filter/map across topic to "fix" the events.
* Events that can't be processed because the consumer fails should be re-processed by the consumer when it is fixed, i.e. downstream timeouts, bugs

## Open Questions

* What is the event categorization to topic mapping? 1:1, Ad-hoc, Grouped...

