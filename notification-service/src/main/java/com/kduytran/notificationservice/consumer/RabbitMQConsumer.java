package com.kduytran.notificationservice.consumer;

import com.kduytran.notificationservice.dto.RegistrationMessageDTO;
import com.kduytran.notificationservice.service.INotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumer.class);

    private final INotificationService notificationService;

    public RabbitMQConsumer(INotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = {"${spring.rabbitmq.queue.name}"})
    public void consume(RegistrationMessageDTO messageDTO){
        LOGGER.info(String.format("Received message -> %s", messageDTO.getUsername()));
        this.notificationService.sendRegistrationEmail(messageDTO);

        // TODO - Add info to database
    }

}
