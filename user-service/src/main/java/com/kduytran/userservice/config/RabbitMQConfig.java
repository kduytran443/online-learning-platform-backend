package com.kduytran.userservice.config;

import com.kduytran.userservice.dto.QueueInfoDTO;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    private final QueueInfoDTO queueInfoDTO;

    public RabbitMQConfig(QueueInfoDTO queueInfoDTO) {
        this.queueInfoDTO = queueInfoDTO;
    }

    // spring bean for rabbitmq exchange
    @Bean
    public DirectExchange exchange(){
        return new DirectExchange(queueInfoDTO.getExchange());
    }

    // spring bean for rabbitmq queue
    @Bean
    public Queue userRegisteredQueue(){
        return new Queue(queueInfoDTO.getUserRegistered().getQueue());
    }

    // binding between queue and exchange using routing key
    @Bean
    public Binding userRegisteredBinding(){
        return BindingBuilder
                .bind(userRegisteredQueue())
                .to(exchange())
                .with(queueInfoDTO.getUserRegistered().getRoutingKey());
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.setConnectionFactory(connectionFactory);
        return rabbitTemplate;
    }

}
