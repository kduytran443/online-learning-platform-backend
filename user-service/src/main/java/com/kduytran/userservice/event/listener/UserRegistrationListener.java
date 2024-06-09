package com.kduytran.userservice.event.listener;

import com.kduytran.userservice.event.UserEvent;
import com.kduytran.userservice.event.UserRegisteredEvent;
import com.kduytran.userservice.utils.LogUtils;
import com.kduytran.userservice.utils.TransactionIdHolder;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@AllArgsConstructor
public class UserRegistrationListener {
    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationListener.class);
    private final KafkaTemplate<String, UserEvent> kafkaTemplate;
    private final TransactionIdHolder transactionIdHolder;
    private final ModelMapper modelMapper;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onUserRegistered(UserRegisteredEvent event) {
        String transactionId = transactionIdHolder.getCurrentTransactionId();
        logger.debug(LogUtils.getLogDebugFormat("Sending user registration event"), transactionId);

        UserEvent userEvent = modelMapper.map(event, UserEvent.class);
        userEvent.setAction("CREATE");
        userEvent.setTransactionId(transactionId);

        kafkaTemplate.send("t_users", event.getId(), userEvent);
    }

}
