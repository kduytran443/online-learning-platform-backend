package com.kduytran.userservice.event.handler;

import com.kduytran.userservice.dto.msg.RegistrationMessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducer {

//    @Value("${queue-list.exchange}")
//    private String exchange;
//
//    @Value("${queue-list.user-registered.routing-key}")
//    private String routingKey;
//
//    @Value("${queue-list.user-registered.queue}")
//    private String queue;
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQProducer.class);
//
//    private RabbitTemplate rabbitTemplate;
//
//    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
//        this.rabbitTemplate = rabbitTemplate;
//    }
//
//    public void sendMessage(RegistrationMessageDTO messageDTO){
//        LOGGER.info(String.format("Message sent -> %s", messageDTO.getUsername()));
//        rabbitTemplate.convertAndSend(exchange, routingKey, messageDTO);
//    }

}
