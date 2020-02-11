package com.streams;

import com.streams.model.Order;
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class FraudDetectionApplication {

    private static Logger LOG = LoggerFactory.getLogger(FraudDetectionApplication.class);


    public static void main(String[] args) {

        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "fraud-detection-app"); // groupId for hte consumer among others use cases
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9093,localhost:9094");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SpecificAvroSerde.class);
        props.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");

        /*
         * SERDE - SERialize and DEsirialize
         * A Streaming APP needs both the serializer and the deserializer, thats why using SERDE here.
         */

        StreamsBuilder streamsBuilder = new StreamsBuilder();

        // Topic we want to consume messages from
        KStream<String, Order> stream = streamsBuilder.stream("payments");


        stream   // Printing all receiving message -> Debugging
                .peek(FraudDetectionApplication::printOnEnter)
                // Business logic filters
                .filter((transactionId, order) -> !order.getUserId().toString().equals(""))
                .filter((transactionId, order) -> order.getNbOfItems() < 1000)
                .filter((transactionId, order) -> order.getTotalAmount() < 10000)
                // Transform all user's ids to uppercase
                .mapValues((order) -> {
                    order.setUserId(String.valueOf(order.getUserId()).toUpperCase());
                    return  order;
                })
                // Printing all receiving message -> Debugging
                .peek(FraudDetectionApplication::printOnExit)
                // Produce it to topic: validated-payments
                .to("validated-payments");

        Topology topology = streamsBuilder.build();

        KafkaStreams kafkaStreams = new KafkaStreams(topology, props);

        kafkaStreams.start(); // Runs Streaming app in a background thread !

        // Finish program: Close all connections to kafka and the app will be stopped.
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));

    }

    private static void printOnEnter(String transactionId, Order order) {
        LOG.info("\n****************************** ***** **** *** *** ***************************");
        LOG.info("ENTERING Stream transaction with ID: " + transactionId +
                " >, of user < " + order.getUserId() + " >, total amount < " + order.getTotalAmount() +
                " >, and nb of items < " + order.getNbOfItems() + " >");
    }

    private static void printOnExit(String transactionId, Order order) {
        LOG.info("\n****************************** ***** **** *** *** ***************************");
        LOG.info("EXITING Stream transaction with ID: " + transactionId +
                " >, of user < " + order.getUserId() + " >, total amount < " + order.getTotalAmount() +
                " >, and nb of items < " + order.getNbOfItems() + " >");
    }
}
