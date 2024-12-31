package com.kduytran.memberservice.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessageHeaders;

import java.util.function.Consumer;

abstract class BaseKafkaConsumer<K, V> {

    public static final Logger LOGGER = LoggerFactory.getLogger(BaseKafkaConsumer.class);
    public static final String RECEIVED_MESSAGE_HEADERS = "## Received message - headers: {}";
    public static final String PROCESSING_RECORD_KEY_VALUE = "## Processing record - Key: {} | Value: {}";
    public static final String RECORD_PROCESSED_SUCCESSFULLY_KEY = "## Record processed successfully - Key: {} \n";

    protected void processMessage(K key, V value, MessageHeaders headers, Consumer<V> consumer) {
        LOGGER.debug(RECEIVED_MESSAGE_HEADERS, headers);
        LOGGER.debug(PROCESSING_RECORD_KEY_VALUE, key, value);
        consumer.accept(value);
        LOGGER.debug(RECORD_PROCESSED_SUCCESSFULLY_KEY, key);
    }
}
