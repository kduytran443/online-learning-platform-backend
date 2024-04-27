package com.kduytran.notificationservice.event.handler;

import com.kduytran.notificationservice.dto.QueueInfoDTO;
import com.kduytran.notificationservice.dto.RegistrationMessageDTO;
import com.kduytran.notificationservice.event.UserRegisteredEvent;
import com.kduytran.notificationservice.service.INotificationService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationEventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationEventHandler.class);
    private final QueueInfoDTO queueInfoDTO;
    private final INotificationService notificationService;
    private final ModelMapper modelMapper;

    public NotificationEventHandler(QueueInfoDTO queueInfoDTO, INotificationService notificationService, ModelMapper modelMapper) {
        this.queueInfoDTO = queueInfoDTO;
        this.notificationService = notificationService;
        this.modelMapper = modelMapper;
    }

    @RabbitListener(queues = {"${queue-list.user-registered.queue}"})
    public void handle(UserRegisteredEvent event) {
        LOGGER.info(String.format("Received message -> %s", event.getUsername()));
        this.notificationService.sendRegistrationEmail(
                this.modelMapper.map(event, RegistrationMessageDTO.class)
        );
    }

}
