package com.kduytran.notificationservice.event.handler;

import com.kduytran.notificationservice.dto.RegistrationMessageDTO;
import com.kduytran.notificationservice.service.INotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationEventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationEventHandler.class);

    private final INotificationService notificationService;

    public NotificationEventHandler(INotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = {"${queue-list.user-registered.queue}"})
    public void consume(RegistrationMessageDTO messageDTO){
        LOGGER.info(String.format("Received message -> %s", messageDTO.getUsername()));
        this.notificationService.sendRegistrationEmail(messageDTO);

        // TODO - Add info to database
    }

}
