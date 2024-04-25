package com.kduytran.userservice.event.listener;

import com.kduytran.userservice.dto.QueueInfoDTO;
import com.kduytran.userservice.event.UserRegisteredEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class UserRegistrationListener {
    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationListener.class);
    private final RabbitTemplate rabbitTemplate;
    private final QueueInfoDTO queueInfo;

    public UserRegistrationListener(RabbitTemplate rabbitTemplate, QueueInfoDTO queueInfo) {
        this.rabbitTemplate = rabbitTemplate;
        this.queueInfo = queueInfo;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onUserRegistered(UserRegisteredEvent event) {
        logger.debug("Sending user registered event to {}, event: {}", queueInfo.getUserRegistered().getQueue(), event);

        rabbitTemplate.convertAndSend(queueInfo.getExchange(), queueInfo.getUserRegistered().getRoutingKey(), event);
    }

}
