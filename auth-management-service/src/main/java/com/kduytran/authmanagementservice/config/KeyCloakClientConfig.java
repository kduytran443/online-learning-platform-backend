package com.kduytran.authmanagementservice.config;

import com.kduytran.authmanagementservice.properties.KeyCloakProps;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class KeyCloakClientConfig {

    private final KeyCloakProps keyCloakProps;

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(keyCloakProps.getAuthServerUrl())
                .realm(keyCloakProps.getRealm())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(keyCloakProps.getResource())
                .clientSecret(keyCloakProps.getSecret())
                .build();
    }

    public static Keycloak getKeycloakWithPasswordGrantType(KeyCloakProps keyCloakProps, String username, String password) {
        return KeycloakBuilder.builder()
                .serverUrl(keyCloakProps.getAuthServerUrl())
                .realm(keyCloakProps.getRealm())
                .grantType(OAuth2Constants.PASSWORD)
                .clientId(keyCloakProps.getResource())
                .clientSecret(keyCloakProps.getSecret())
                .username(username)
                .password(password)
                .build();
    }
}
