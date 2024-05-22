package com.kduytran.classservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@EnableFeignClients(basePackages = "com.kduytran.classservice.service.client")
@OpenAPIDefinition(
		info = @Info(
				title = "Class microservice REST API Documentation",
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
public class ClassServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClassServiceApplication.class, args);
	}

}
