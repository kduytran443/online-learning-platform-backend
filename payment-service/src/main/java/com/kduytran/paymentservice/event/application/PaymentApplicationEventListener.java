package com.kduytran.paymentservice.event.application;

import com.kduytran.paymentservice.constant.KafkaConstant;
import com.kduytran.paymentservice.event.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@AllArgsConstructor
@Setter @Getter
public class PaymentApplicationEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentApplicationEventListener.class);
    private KafkaTemplate<String, PaymentCreatedEvent> createdEventKafkaTemplate;
    private KafkaTemplate<String, PaymentExecutedEvent> executedEventKafkaTemplate;
    private KafkaTemplate<String, PaymentFailedEvent> failedEventKafkaTemplate;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onPaymentCreated(PaymentCreatedEvent event) {
        log(event);
        createdEventKafkaTemplate.send(KafkaConstant.TOPIC_PAYMENTS, event.getId().toString(), event);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onPaymentExecuted(PaymentExecutedEvent event) {
        log(event);
        executedEventKafkaTemplate.send(KafkaConstant.TOPIC_PAYMENTS, event.getId().toString(), event);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onPaymentCancelled(PaymentCancelledEvent event) {
        log(event);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onPaymentFailed(PaymentFailedEvent event) {
        log(event);
        failedEventKafkaTemplate.send(KafkaConstant.TOPIC_PAYMENTS, event.getId().toString(), event);
    }

    private void log(AbstractPaymentEvent event) {
        LOGGER.info(String.format("Pushed %s: Action = %s, Id = %s",
                event.getClass().getName(),
                event.getAction(),
                event.getId()));
    }

}
