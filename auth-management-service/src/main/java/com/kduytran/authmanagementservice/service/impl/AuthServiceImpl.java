package com.kduytran.authmanagementservice.service.impl;

import com.kduytran.authmanagementservice.exception.KeyCloakException;
import com.kduytran.authmanagementservice.properties.KeyCloakProps;
import com.kduytran.authmanagementservice.service.AuthService;
import com.kduytran.authmanagementservice.service.client.KeyCloakClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.token.TokenManager;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.kduytran.authmanagementservice.config.KeyCloakClientConfig.getKeycloakWithPasswordGrantType;

@Slf4j
@Service
@RequiredArgsConstructor
class AuthServiceImpl implements AuthService {

    private final KeyCloakClient keyCloakClient;
    private final KeyCloakProps keyCloakProps;

    @Override
    public AccessTokenResponse login(String username, String password) {
        Keycloak client = getKeycloakWithPasswordGrantType(keyCloakProps, username, password);
        TokenManager tokenmanager = client.tokenManager();
        return tokenmanager.getAccessToken();
    }

    @Override
    public void logout(String refreshToken) {
        Map<String, String> form = new HashMap<>();
        form.put(OAuth2Constants.CLIENT_ID, keyCloakProps.getResource());
        form.put(OAuth2Constants.CLIENT_SECRET, keyCloakProps.getSecret());
        form.put(OAuth2Constants.REFRESH_TOKEN, refreshToken);
        try {
            var response = keyCloakClient.logout(keyCloakProps.getRealm(), form);
            log.info("Logout response status: {}", response.getStatusCode());
        } catch (Exception e) {
            throw new KeyCloakException("Cannot logout");
        }
    }

    @Override
    public AccessTokenResponse refreshToken(String refreshToken) {
        Map<String, String> form = new HashMap<>();
        form.put(OAuth2Constants.GRANT_TYPE, OAuth2Constants.REFRESH_TOKEN);
        form.put(OAuth2Constants.REFRESH_TOKEN, refreshToken);
        form.put(OAuth2Constants.CLIENT_ID, keyCloakProps.getResource());
        form.put(OAuth2Constants.CLIENT_SECRET, keyCloakProps.getSecret());
        try {
            return keyCloakClient.refreshToken(keyCloakProps.getRealm(), form);
        } catch (Exception e) {
            throw new KeyCloakException("Cannot refresh token");
        }
    }
}
