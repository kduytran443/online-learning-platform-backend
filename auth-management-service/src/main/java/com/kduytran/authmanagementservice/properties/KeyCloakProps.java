package com.kduytran.authmanagementservice.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(value = "keycloak")
public class KeyCloakProps {
    private String authServerUrl;
    private String realm;
    private String resource; // Client ID
    private String secret;
}
