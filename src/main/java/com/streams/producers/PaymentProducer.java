package com.streams.producers;

import com.leinad.EventGenerator;
import com.leinad.models.Event;
import com.leinad.models.Product;
import com.leinad.models.User;
import com.streams.FraudDetectionApplication;
import com.streams.model.Order;
import com.streams.model.PaymentEvent;
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.StreamsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.Random;

public class PaymentProducer {

    private static Logger LOG = LoggerFactory.getLogger(PaymentProducer.class);

    public static void main(String[] args) throws InterruptedException {

        Properties props = new Properties();
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9093,localhost:9094");
        props.put("key.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
        props.put("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
        props.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");

        Producer<String, Order> kafkaProducer = new KafkaProducer<String, Order>(props);

        // Lets producer 10 events
        for (int i = 1; i <= 10; i++) {
            PaymentEvent event = getRandomPaymentEvent();

            String key = extractKey(event);
            Order value = extractValue(event);

            // Record (message to be sent ot kafka)
            ProducerRecord<String, Order> record = new ProducerRecord<String, Order>("payments", key, value);

            LOG.info("Producing to kafka the record: " + key + ": " + value);
            kafkaProducer.send(record);

            Thread.sleep(1000);
        }

        // Close the connection to kafka !!!
        kafkaProducer.close();
    }

    private static PaymentEvent getRandomPaymentEvent() {
        Random r = new Random();
        Order order = Order.newBuilder()
                .setNbOfItems((r.nextInt(11)))
                .setTotalAmount(r.nextInt((500 - 100) + 1) + 100)
                .setUserId("leinad-" + r.nextInt(500))
                .build();
        return PaymentEvent.builder()
                .transactionId("payment-" + r.nextInt(900))
                .order(order)
                .build();
    }

    private static String extractKey(PaymentEvent event) {
        return event.getTransactionId().toUpperCase();
    }

    private static Order extractValue(PaymentEvent event) {
        return Order.newBuilder()
                .setNbOfItems(event.getOrder().getNbOfItems())
                .setTotalAmount(event.getOrder().getTotalAmount())
                .setUserId(event.getOrder().getUserId())
                .build();
    }
}
