package com.research.randy.logging.kafka;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.CompletableFuture;


@Service
public class kafkaProducer {

    private static final Logger logger = LoggerFactory.getLogger(kafkaProducer.class);
    private static final String TOPIC = "randyResearchLog";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    public void sendMessage(String key, String value) {
        logger.info(String.format("Produced event to topic %s: key = %-10s value = %s", TOPIC, key, value));
        // Send message and get CompletableFuture
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(TOPIC, key, value);
        future.thenAccept(result -> {
            // Log success
            logger.info(String.format("Produced event to topic %s: key = %-10s value = %s", TOPIC, key, value));
        }).exceptionally(ex -> {
            // Handle failure
            logger.error("Failed to send message to Kafka", ex);
            return null;
        });
    }
}