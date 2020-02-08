Designing Event-driven Applications Using Apache Kafka Ecosystem

Messages
Basic unit of communication. Can be anything.

Events
A message that informs various listeners about something that has happened.

Commands
Present a targeted action by having a one-to-one connection between a producer and a consumer.

EDA (Event Driven Architecture) benefits:

1. All components are decoupled one of the other. Broker-based technology to communicate from A to B, for ex. Direct communication between service A to B is forbidden, because It leads to a titghly situation.
2. Encapsulation. Events can be encapsulated between different boundaries, and processed under the same boundaries.
3. Optimization. Works almost in real time due to its architecture.
4. Scalability. Application accommodate a growing amount of work, as well as cutting down resources when no needed.

EDA drawbacks:

1. Steep learning curve.
2. Complexity.
3. Loss of transactionality.
4. Lineage (to solve this provide a unique ID to each event, to determine from which app it came from).

Event Storming & DDD

Typical system -> Resolves addons based on data (database)
ED systems -> Resolves adding based on events. How events interact with and in your system, and how there going to be processed.
Common approach to ED system is Event Storming with Event-driven Design (DDD)

Model Events in EDA
-> Common approach: By using a workshop called Event Storming with DDD.

Event Storming
Used to model a whole business line with domain events as a result of collaboration of different members of your organization.

Few things to take in place in order to facilitate the workshop:

1. Room big enough for people participating in it. As a suggestion, try to move tables a chairs away. Its a interactive workshop and people should move around freely.
2. Inivte the right people: Facilitator, the person who knows how the worksop works, architects, developers, UX team, and a Domain Expert.
3. Huge piece of paper, tapped on the wall.
4. Sticky notes: All the events will be written in sticky notes on different colors.
5. The workshop can take the entire day or several days.
6. Have some food for people in the worksop

The sticky notes used in the workshop correspondence to a color scheme:

- Domain events
  Color: Orange
  Definition: It’s something that’s happening in the system and is relevant to the business. Usually a domain event provokes a reaction, maybe even another event, and your system needs to react to them.
- Policies events
  Color: Purple
  Definition: Represents a process occurred by an event and it always starts with the keyword “Whenever…”. For example: Whenever a new account has been created, we send a email of confirmation.
- External Systems
  Color: Pink
  Definition: Defines any interaction with applications that are managed by someone else and you don’t have control over it. An example of such, would be a external payment provider like PayPal.
- Commands
  Color: Blue
  Definition: Actions initiated by a user or by a system like a scheduled job. The only difference between a Command and an Event is the fact that commands usually resides at the beginning of an event flow, triggering the event chain.

Once the business model is complete, and all the bottlenecks has been addressed, we can step in the solution space using Event Driven Design (DDD):

Event Driven Design (DDD):
To model the software we first need to identify he aggregate by logically grouping various commands and events together. The goal is to define structures that are isolating the related concerns from one to another. When all the aggregate has been defined it’s time to define de Bounded Context.
Aggregates groups relates behaviors together, whereas bounded context group meaning hallowing the use of the same terms in different sub-domains.
For instance, “Received” in an order system has a completing different meaning to “Received” in the Shipping system.

Web-based apps to help with Event Storming and DDD: https://miro.com/app/

Sample of Event Storming:
<img src="https://github.com/leinadpb/kafka/blob/master/src/main/resources/event_storming.png" />

SAMPLE OF DDD -> Divided into Aggregations and Bounded context:

Aggregations:
<img src="https://github.com/leinadpb/kafka/blob/master/src/main/resources/aggregations.png" />

Bounded context with aggregations:
<img src="https://github.com/leinadpb/kafka/blob/master/src/main/resources/bounded_ctx.png" />

Kafka
Distributed Streaming Platform

- Only works with byte code
- Does not Serialize or deserialize objects (best for performance)
- When persisting, bypass the Java Heap (RAM) and go directly to the disk, this is collar Non-copy strategy. But, for TLS connections this is not possible, because this library is in the JDK and it needs to pass for the Java Heap before going to disk.

All uses:

- Messaging system (Publish / Susbsribe patterns): Use the Producer and consumer pattern. Producers publish events, and consumers subscribe to hose events and consume them.
- Distributed Storage: Can be used for storing the date in a distributing way.
- Data Processing: Process the events as they occurs.

Kafka components:

- Broker: Deals with receiving, storing and transmitting data. Cannot live by itself, it requires a Zookeeper in order to achieve a distributed state.
- Zookeeper: Electing a controller (responsible to make partitions), cluster membership, topic configuration, quotas, ACLs, with consumer group exists.

A Broker should ALWAYS resides in a dedicated server with dedicated resources, since it has a very high input/output load, which may affect other apps
A Broker is nothing more than a process which lives on top of the operating system.
When a message is received across the network, the broker stores it into the hard drive of that machine. When that specific message has been requested, the Broker will then copy it form the File System and will transmitted to the application that requested it.
Everything is done at the binary level. An messages are nothing more that bytes received over the network.
<img src="https://github.com/leinadpb/kafka/blob/master/src/main/resources/broker.png" />

For production apps, where load will be really high, multiple Brokers have to need added under the same context, this distribution is called a Cluster.

So, here enters the Zookeeper as a centralized server where all the brokers are connected in order to ensure distributed transactions:
<img src="hhttps://github.com/leinadpb/kafka/blob/master/src/main/resources/zookeeper.png" />

Messages in Kafka ara named Record.

A record:

- Key
- Value
- Timestamp

Both key and value can be represented by any kind of information.
The default limit for a size of a message is 1MB.
If you don’t specify timestamp manually, then the Producer will do it for you.

Messages can be categorized:

- Topics
  Are stored in a broker.
  A list of all topics is managed by the Zookeeper.

Sample scenario:
Topics: Account, Cart Payment

<img src="https://github.com/leinadpb/kafka/blob/master/src/main/resources/topics.png" />

Types of Topics:

DELETE: Is deleting the data based on some factors:

1. Size: When the current size of massages in the topics reach the max size, and another message comes in, the oldest message will be removed.
2. Time: By default a Broker is configured to store messages for 7 days. Pass this time, messages on broker will be removed.

COMPACTION: Mimicking Upsert (update and insert) functionality from a Database.
When inserting, if a message comes in with the same key as one already in the broker, the message will be updated. If the key does not exists it will simply insert it.

Kafka uses partitions to handle the distributed load in the clusters servers, and replicating the data across the servers in the cluster.
The way this works is: Each topic is split into multiple partitions, each partition living in a separate Broker. Multiple partitions can reside in the same Broker when the number of partitions is bigger than the number of Brokers.
Each partition will store different messages.

Replicated partitions
Replicas of original partitions are stored in each Broker. This avoids data loss in case a Broker stop working, ensuring the integrity of the data.
A copy of the message will automatically sent to the replica in the other Broker.

<img src="https://github.com/leinadpb/kafka/blob/master/src/main/resources/rep_partitions.png" />

Kafka Producer
Is an app that creates and transfer events to the Kafka cluster.
Serialization of the Record (key and value) needs to be done at this stage before sending it to Kafka.
<img src="https://github.com/leinadpb/kafka/blob/master/src/main/resources/producer.png" />

Kafka Consumer
When new messages has arrived to the listeners, the Kafka consumers will received them and consumes them accordingly.
Uses a Pull mechanism: When a message is available in the Kafka cluster, the consumer will pull and process it, allowing the app to consume messages in its own rhythm.

The consumer is always asking the Kafka cluster every couple of milliseconds.. Do you have any message for me?
When a new message has arrived on topic, the consumer will:

1. Pull it
2. Creates a consumer record
3. Deserialize the message record (key and value)
4. Ready to process it

Communicating Messaging structure using AVRO and Schema Registry —>
AVRO - Open source serialization format
Schema Registry - Facilitates transferring data type between apps.

Serialization
The process of translating data structures or object states into a format that can be stored or transmitted and reconstructed later, possibly in a different computer environment.

AVRO -> Data serialization system that serializes to Binary and uses Schema IDL
Uses JSON-Based schemas to define data structures.
No human readable
Binary
<img src="https://github.com/leinadpb/kafka/blob/master/src/main/resources/avro.png" />
￼

Schema Registry
Persisten schemas and share them between Producers and Consumers.

<img src="https://github.com/leinadpb/kafka/blob/master/src/main/resources/schema_registry.png" />

The producer or consumer ask a sham to the SR (Schema Registry), using a standard format, ex: <name>-key.
The Schema ID then, is the ID of the message sent to Kafka, so the consumer (with this ID) can ask for the corresponding schema in the Schema Registry, so deserialization can take place.

In a production environment an administrator should upload the schemas to the Schema Registry.

Schema Registry stores the schemas in-memory. But it has a certain kind of backup in the Kafka Cluster. So, if the Schema Registry fails, a new instance is created and ask for the backup in the Kafka Cluster.

Confluent Community Licensing: You can access source code and modify it or redistributed it; there is only one thing you cannot do, and that is use it to make a competiting Sass offering. - Confluent Website

Setting up Schema Registry
Github: <a href="https://github.com/confluentinc/schema-registry.git">https://github.com/confluentinc/schema-registry.git</a>

Checkout to the latest version.
Run: man package

Now is able to run: bin/schema-registry-start config/schema-registry.properties
