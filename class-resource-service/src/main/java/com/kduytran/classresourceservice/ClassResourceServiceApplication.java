package com.kduytran.classresourceservice;

import com.kduytran.classresourceservice.service.client.AuthServiceClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@EnableCaching
@EnableFeignClients(clients = {AuthServiceClient.class})
public class ClassResourceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClassResourceServiceApplication.class, args);
    }

}
