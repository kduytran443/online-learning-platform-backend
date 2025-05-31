package com.kduytran.paymentservice.event.application;

import com.kduytran.paymentservice.PaymentServiceApplication;
import com.kduytran.paymentservice.constant.KafkaConstant;
import com.kduytran.paymentservice.event.PaymentCreatedEvent;
import com.kduytran.paymentservice.event.PaymentExecutedEvent;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = PaymentServiceApplication.class)
@ActiveProfiles("ut")
@EmbeddedKafka(
        partitions = 4,
        brokerProperties = {
                "listeners=PLAINTEXT://localhost:29092",
                "port=29092"
        },
        topics = {"t_payments"})
public class PaymentApplicationEventListenerTest {

    @Mock
    private KafkaTemplate<String, PaymentCreatedEvent> createdEventKafkaTemplate;

    @Mock
    private KafkaTemplate<String, PaymentExecutedEvent> executedEventKafkaTemplate;

    @InjectMocks
    private PaymentApplicationEventListener listener;

    @Test
    void testPaymentCreatedEvent() {
        PaymentCreatedEvent event = new PaymentCreatedEvent();
        event.setId(UUID.randomUUID());
        listener.onPaymentCreated(event);
        verify(createdEventKafkaTemplate, times(1))
                .send(eq(KafkaConstant.TOPIC_PAYMENTS), anyString(), eq(event));
    }

    @Test
    void testPaymentExecuted() {
        PaymentExecutedEvent event = new PaymentExecutedEvent();
        event.setId(UUID.randomUUID());
        listener.onPaymentExecuted(event);
        verify(executedEventKafkaTemplate, times(1))
                .send(eq(KafkaConstant.TOPIC_PAYMENTS), anyString(), eq(event));
    }

}
