package com.kduytran.authmanagementservice.listener;

import com.kduytran.authmanagementservice.event.UserRegisteredEvent;
import com.kduytran.authmanagementservice.exception.SignUpNotValidException;
import com.kduytran.authmanagementservice.kafka.sender.UserMessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
class UserEventListener {

    private final UserMessageSender userMessageSender;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    void onUserRegisteredEvent(UserRegisteredEvent event) {
        throw new SignUpNotValidException("..");
        // userMessageSender.sendMessage(event);
    }
}
