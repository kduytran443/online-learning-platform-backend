package com.kduytran.authmanagementservice.service;

import org.keycloak.representations.AccessTokenResponse;

public interface AuthService {

    AccessTokenResponse login(String username, String password);

    void logout(String refreshToken);

    AccessTokenResponse refreshToken(String refreshToken);
}
