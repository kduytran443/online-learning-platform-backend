package com.kduytran.authmanagementservice.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;

import java.util.function.Supplier;

@Configuration
public class KeyCloakConfig {

    @Autowired
    Environment env;

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Supplier<Keycloak> keycloakInstance() {
        return () -> KeycloakBuilder.builder()
                .serverUrl(env.getProperty("keycloak.auth-server-url"))
                .realm(env.getProperty("keycloak.realm"))
                .clientId(env.getProperty("keycloak.resource"))
                .clientSecret(env.getProperty("keycloak-config.client-secret"))
                .grantType("client_credentials")
                .build();
    }

}
