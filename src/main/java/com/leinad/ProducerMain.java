package com.leinad;

import com.leinad.models.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Date;
import java.util.Properties;

@Slf4j
public class ProducerMain {

    public static void main(String[] args) throws InterruptedException {

        EventGenerator eventGenerator = new EventGenerator();

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9093,localhost:9094");
        props.put("key.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
        props.put("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
        props.put("schema.registry.url", "http://localhost:8081");

        Producer<User, Product> kafkaProducer = new KafkaProducer<User, Product>(props);

        // Lets producer 10 events
        for (int i = 1; i <= 10; i++) {
            log.info("Generating event #" + i);

            // Event
            Event event = eventGenerator.generateEvent();

            User key = extractKey(event);
            Product value = extractValue(event);

            // Record (message to be sent ot kafka)
            ProducerRecord<User, Product> record = new ProducerRecord<User, Product>("user-tracking-avro", key, value);

            log.info("Producing to kafka the record: " + key + ": " + value);
            kafkaProducer.send(record);

            Thread.sleep(1000);
        }

        // Close the connection to kafka !!!
        kafkaProducer.close();
    }

    private static User extractKey(Event event) {
        return User.newBuilder()
                .setUserId(event.getUser().getUserId().toString())
                .setUsername(event.getUser().getUsername())
                .setDateOfBirth((int) event.getUser().getDateOfBirth().getTime() )
                .build();
    }

    private static Product extractValue(Event event) {
        return Product.newBuilder()
                .setColor(event.getProduct().getColor().toString())
                .setDesignType(event.getProduct().getDesignType().toString())
                .setType(event.getProduct().getType().toString())
                .build();
    }

}
