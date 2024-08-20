package com.kduytran.orderservice.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@Configuration
@EnableKafka
public class KafkaAdminAPIConfig {

    @Bean
    public NewTopic ordersTopic() {
        return new NewTopic(KafkaConstant.TOPIC_ORDERS, 4, (short) 2);
    }

}
