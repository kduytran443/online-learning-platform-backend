package com.kduytran.classservice;

import com.kduytran.classqueryservice.ClassQueryServiceApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
		classes = ClassQueryServiceApplication.class,
		properties = "spring.config.additional-location=classpath:/application-unittest.yml"
)
@EmbeddedKafka(
		partitions = 1,
		brokerProperties = {
				"listeners=PLAINTEXT://localhost:29092",
				"port=29092"
		},
topics = {"t_categories", "t_classes"})
@ActiveProfiles("unittest")
class ClassQueryServiceApplicationTests {

	@Test
	void contextLoads() {

	}

}
