package com.kduytran.orderservice.config;

import com.kduytran.orderservice.constant.KafkaConstant;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@Configuration
@EnableKafka
@EnableKafkaStreams
public class KafkaAdminAPIConfig {

    @Bean
    public NewTopic ordersTopic() {
        return new NewTopic(KafkaConstant.TOPIC_ORDERS, 4, (short) 1);
    }

}
