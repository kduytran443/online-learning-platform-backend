package com.kduytran.paymentservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("ut")
@EmbeddedKafka(
		partitions = 4,
		brokerProperties = {
				"listeners=PLAINTEXT://localhost:29092",
				"port=29092"
		},
		topics = {"t_payments", "t_orders"})
@SpringBootTest
class PaymentServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
