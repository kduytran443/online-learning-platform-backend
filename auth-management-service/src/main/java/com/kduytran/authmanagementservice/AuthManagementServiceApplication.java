package com.kduytran.authmanagementservice;

import com.kduytran.authmanagementservice.properties.KeyCloakProps;
import com.kduytran.authmanagementservice.properties.OlpSecurityProperties;
import com.kduytran.authmanagementservice.service.client.KeyCloakClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(clients = {KeyCloakClient.class})
@EnableConfigurationProperties(value = {OlpSecurityProperties.class, KeyCloakProps.class})
@SpringBootApplication
@EnableCaching
public class AuthManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthManagementServiceApplication.class, args);
    }

}
