package com.kduytran.paymentservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("ut")
@EmbeddedKafka
@SpringBootTest
class PaymentServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
