package com.leinad;

import com.leinad.models.InternalProduct;
import com.leinad.models.InternalUser;
import com.leinad.models.Product;
import com.leinad.models.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ConsumerMain {

    private static void processRecord(ConsumerRecord<User, Product> record) {
        log.info("Processng record: " + record.key() + ": "  + record.value());
    }

    private static List<String> asList(String... values) {
        List<String> list = new ArrayList<String>();
        list.addAll(Arrays.asList(values));
        return list;
    }

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9093,localhost:9094");
        props.put("group.id", "user-tracking-consumer");
        props.put("key.deserializer", "io.confluent.kafka.serializers.KafkaAvroDeserializer");
        props.put("value.deserializer", "io.confluent.kafka.serializers.KafkaAvroDeserializer");
        props.put("schema.registry.url", "http://localhost:8081");
        props.put("specific.avro.reader", "true");

        KafkaConsumer<User, Product> kafkaConsumer = new KafkaConsumer<User, Product>(props);

        kafkaConsumer.subscribe(asList("user-tracking-avro"));

        // Keep consumer alive
        while(true) {
            ConsumerRecords<User, Product> records = kafkaConsumer.poll(Duration.ofMillis(100));

            for (ConsumerRecord<User, Product> record: records) {

                // Process the record retreived from kafka ! Implement your business logic here too.
                processRecord(record);
            }
        }

    }
}
