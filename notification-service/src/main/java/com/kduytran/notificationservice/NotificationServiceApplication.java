package com.kduytran.notificationservice;

import com.kduytran.notificationservice.dto.QueueInfoDTO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({ QueueInfoDTO.class })
@SpringBootApplication(scanBasePackages = "com.kduytran.notificationservice")
public class NotificationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationServiceApplication.class, args);
	}

}
