package com.kduytran.authmanagementservice;

import com.kduytran.authmanagementservice.properties.OlpSecurityProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(value = {OlpSecurityProperties.class})
@SpringBootApplication
public class AuthManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthManagementServiceApplication.class, args);
    }

}
