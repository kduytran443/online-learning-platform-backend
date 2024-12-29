package com.kduytran.memberservice.kafka.consumer;

import com.kduytran.memberservice.constant.KafkaConstant;
import com.kduytran.memberservice.event.PaymentEvent;
import com.kduytran.memberservice.service.IClassMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.RetriableException;
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

@Component
@RequiredArgsConstructor
public class PaymentConsumer {

    private final IClassMemberService classMemberService;

    @KafkaListener(
            id = "payment-handling",
            groupId = "member-consumers",
            topics = KafkaConstant.TOPIC_PAYMENTS
    )
    @RetryableTopic(    // Non-blocking
            attempts = "3", // 1 primary topic + 3 retry topics + 1 DLT topic
            backoff = @Backoff(delay = 1000, multiplier = 2), // exponential backoff is better than linear backoff
            // here - 1, 2, 4
            dltStrategy = DltStrategy.FAIL_ON_ERROR, // No retry on DLT topic
            autoCreateTopics = "true",  // true: for test; false: recommended
            include = {RetriableException.class, RuntimeException.class}    // RuntimeException: for test; mark
            // Retriable business exceptions
    )
    public void processMessage(
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Payload(required = false) @Valid PaymentEvent paymentEvent,
            @Headers MessageHeaders headers) {
        // Handling
    }
}
