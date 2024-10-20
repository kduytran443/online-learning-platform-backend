package com.kduytran.userservice.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {
    public static final String USER_TOPIC = "t_users";

    @Bean
    public NewTopic userTopic() {
        return new NewTopic(USER_TOPIC, 2, (short) 1);
    }

}
