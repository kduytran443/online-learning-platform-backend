package com.kduytran.memberservice.kafka.consumer;

import com.kduytran.memberservice.constant.KafkaConstant;
import com.kduytran.memberservice.event.PaymentEvent;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.RetriableException;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class PaymentConsumer extends BaseKafkaConsumer<String, PaymentEvent> {

    private final PaymentConsumerService paymentConsumerService;

    @KafkaListener(
            id = "payment-handling",
            groupId = "member-consumers",
            topics = KafkaConstant.TOPIC_PAYMENTS
    )
    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 1000, multiplier = 2),
            dltStrategy = DltStrategy.FAIL_ON_ERROR, // No retry on DLT topic
            autoCreateTopics = "true", // true: for test; false: recommended
            include = {RetriableException.class, RuntimeException.class}
    )
    public void processMessage(
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Payload(required = false) @Valid PaymentEvent paymentEvent,
            @Headers MessageHeaders headers) {
        processMessage(key, paymentEvent, headers, paymentConsumerService::handle);
    }

    @DltHandler
    public void handleDlt(@Payload String message) {
        log.debug("DLT handler");
    }
}
