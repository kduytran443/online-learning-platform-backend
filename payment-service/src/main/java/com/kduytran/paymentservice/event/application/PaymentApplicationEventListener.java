package com.kduytran.paymentservice.event.application;

import com.kduytran.paymentservice.event.AbstractPaymentEvent;
import com.kduytran.paymentservice.event.PaymentCancelledEvent;
import com.kduytran.paymentservice.event.PaymentCreatedEvent;
import com.kduytran.paymentservice.event.PaymentExecutedEvent;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@AllArgsConstructor
public class PaymentApplicationEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentApplicationEventListener.class);

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onPaymentCreated(PaymentCreatedEvent event) {
        log(event);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onPaymentExecuted(PaymentExecutedEvent event) {
        log(event);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onPaymentCancelled(PaymentCancelledEvent event) {
        log(event);
    }

    private void log(AbstractPaymentEvent event) {
        LOGGER.info(String.format("Pushed %s: Action = %s, Id = %s",
                event.getClass().getName(),
                event.getAction(),
                event.getId()));
    }

}
