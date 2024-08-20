package com.kduytran.orderservice.event.application;

import com.kduytran.orderservice.event.OrderCreatedEvent;
import com.kduytran.orderservice.constant.KafkaConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderEventApplicationListener {
    private final KafkaTemplate<String, OrderCreatedEvent> createdKafkaTemplate;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOrderCreated(OrderCreatedEvent event) {
        log.info("Transaction ID: {} - Sending orderCreated event for order ID: {}",
                event.getCorrelationId(), event.getOrderId());
        createdKafkaTemplate.send(KafkaConstant.TOPIC_ORDERS, event.getOrderId().toString(), event);
    }

}
