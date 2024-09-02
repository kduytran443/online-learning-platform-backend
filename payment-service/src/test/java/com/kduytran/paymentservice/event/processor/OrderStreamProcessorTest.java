package com.kduytran.paymentservice.event.processor;


import com.kduytran.paymentservice.PaymentServiceApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = PaymentServiceApplication.class)
@ActiveProfiles("ut")
@EmbeddedKafka(
        partitions = 4,
        brokerProperties = {
                "listeners=PLAINTEXT://localhost:29092",
                "port=29092"
        },
        topics = {"t_payments"}
)
public class OrderStreamProcessorTest {

    public void testHandleStream() {

    }

}
