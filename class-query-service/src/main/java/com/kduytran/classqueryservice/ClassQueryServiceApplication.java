package com.kduytran.classqueryservice;

import com.kduytran.classqueryservice.dto.ServiceContactInfoDTO;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@OpenAPIDefinition(
		info = @Info(
				title = "Class query microservice REST API Documentation",
				description = "Java Microservices project for online learning platform",
				version = "v1",
				contact = @Contact(
						name = "Tran Khanh Duy",
						email = "trankhanhduy18@gmail.com"
				),
				license = @License(
						name = "Apache 2.0"
				)
		)
)
@SpringBootApplication
@EnableKafka
@EnableKafkaStreams
@EnableConfigurationProperties({ ServiceContactInfoDTO.class })
public class ClassQueryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClassQueryServiceApplication.class, args);
	}

}
