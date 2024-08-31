package com.kduytran.pricingservice.config;

import com.kduytran.pricingservice.constant.KafkaConstant;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@Configuration
public class KafkaAdminConfig {

    @Bean
    public NewTopic pricesTopic() {
        return new NewTopic(KafkaConstant.TOPIC_PRICES, 4, (short) 1);
    }

}
