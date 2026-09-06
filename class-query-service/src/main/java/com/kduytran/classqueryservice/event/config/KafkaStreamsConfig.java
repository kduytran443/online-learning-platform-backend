package com.kduytran.classqueryservice.event.config;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

import java.util.Map;

@Configuration
public class KafkaStreamsConfig {

    @Bean
    public KafkaStreamsConfiguration streamsConfig(
            KafkaProperties kafkaProperties,
            SslBundles sslBundles
    ) {
        Map<String, Object> properties =
                kafkaProperties.buildStreamsProperties(sslBundles);
        return new KafkaStreamsConfiguration(properties);
    }
}
