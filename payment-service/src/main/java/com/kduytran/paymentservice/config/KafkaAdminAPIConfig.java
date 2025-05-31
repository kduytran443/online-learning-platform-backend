package com.kduytran.paymentservice.config;

import com.kduytran.paymentservice.constant.KafkaConstant;
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
        return new NewTopic(KafkaConstant.TOPIC_PAYMENTS, 4, (short) 1);
    }

}
