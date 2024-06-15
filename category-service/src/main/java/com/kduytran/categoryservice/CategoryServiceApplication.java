package com.kduytran.categoryservice;

import com.kduytran.categoryservice.dto.ServiceContactInfoDTO;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableCaching
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@EnableConfigurationProperties({ ServiceContactInfoDTO.class })
@OpenAPIDefinition(
		info = @Info(
				title = "Category microservice REST API Documentation",
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
public class CategoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CategoryServiceApplication.class, args);
	}

}
