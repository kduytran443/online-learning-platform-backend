package com.kduytran.notificationservice.consumer;

import com.kduytran.notificationservice.dto.RegistrationMessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumer.class);

    @RabbitListener(queues = {"${spring.rabbitmq.queue.name}"})
    public void consume(RegistrationMessageDTO messageDTO){
        LOGGER.info(String.format("Received message -> %s", messageDTO.getUsername()));
    }
}
