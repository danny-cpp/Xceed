package capstone.xceed.communication;

import java.time.Duration;
import java.util.*;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.*;

public class KafkaExample {
    public static void main(String[] args) throws Exception {

        String topicName = "my-topic";
        String groupName = "my-group";
        String brokerList = "localhost:64000";

        // create producer properties
        Properties producerProps = new Properties();
        producerProps.put("bootstrap.servers", brokerList);
        producerProps.put("acks", "all");
        producerProps.put("retries", 0);
        producerProps.put("batch.size", 16384);
        producerProps.put("linger.ms", 1);
        producerProps.put("buffer.memory", 33554432);
        producerProps.put("key.serializer", StringSerializer.class.getName());
        producerProps.put("value.serializer", StringSerializer.class.getName());

        // create producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(producerProps);

        // create consumer properties
        Properties consumerProps = new Properties();
        consumerProps.put("bootstrap.servers", brokerList);
        consumerProps.put("group.id", groupName);
        consumerProps.put("key.deserializer", StringDeserializer.class.getName());
        consumerProps.put("value.deserializer", StringDeserializer.class.getName());

        // create consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps);

        // subscribe consumer to topic
        consumer.subscribe(Collections.singletonList(topicName));

        // send message to producer
        String message = "Hello, World!";
        ProducerRecord<String, String> record = new ProducerRecord<>(topicName, message);
        producer.send(record);

        // poll for messages from consumer
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> rc : records) {
                System.out.println("Received message: " + rc.value());
            }
        }

        // close producer and consumer
        // producer.close();
        // consumer.close();
    }
}