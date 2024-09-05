package com.research.randy.logging.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;

import java.io.IOException;

@Service
public class kafkaConsumer {

    private final Logger logger = LoggerFactory.getLogger(kafkaConsumer.class);
    @KafkaListener(id = "myConsumer", topics = "randyResearchLog", groupId = "springboot-group-1", autoStartup = "false")
    public void listen(String value,
                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                       @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        logger.info(String.format("\n\n Consumed event from topic %s: key = %-10s value = %s \n\n", topic, key, value));
    }
}