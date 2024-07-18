package com.kduytran.classqueryservice.event.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonSerde;

@Configuration
public class KafkaStreamsConfig {

    @Bean
    public StreamsConfig streamsConfig(KafkaProperties properties) {
        return new StreamsConfig(properties.buildStreamsProperties());
    }

}
