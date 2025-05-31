package com.kduytran.olpcommon.kafka.config;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

public class BaseKafkaListenerConfig<K, V> {

    private final Class<K> keyClass;
    private final Class<V> valueClass;
    private final KafkaProperties kafkaProperties;

    public BaseKafkaListenerConfig(Class<K> keyClass, Class<V> valueClass, KafkaProperties kafkaProperties) {
        this.keyClass = keyClass;
        this.valueClass = valueClass;
        this.kafkaProperties = kafkaProperties;
    }

    public ConcurrentKafkaListenerContainerFactory<K, V> concurrentKafkaListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<K, V>();
        factory.setConsumerFactory(typeConsumerFactory(keyClass, valueClass));
        return factory;
    }

    private ConsumerFactory<K, V> typeConsumerFactory(Class<K> keyClazz, Class<V> valueClazz) {
        Map<String, Object> props = buildConsumerProperties();
        var keyDeserialize = new ErrorHandlingDeserializer<>(gettJsonDeserializer(keyClazz));
        var valueDeserialize = new ErrorHandlingDeserializer<>(gettJsonDeserializer(valueClazz));
        return new DefaultKafkaConsumerFactory<>(props, keyDeserialize, valueDeserialize);
    }

    private Map<String, Object> buildConsumerProperties() {
        return kafkaProperties.buildConsumerProperties(null);
    }

    private static <T> JsonDeserializer<T> gettJsonDeserializer(Class<T> clazz) {
        var jsonDeserializer = new JsonDeserializer<>(clazz);
        jsonDeserializer.addTrustedPackages("*");
        return jsonDeserializer;
    }
}
