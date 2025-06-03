package com.kduytran.authmanagementservice.service.client;

import org.keycloak.representations.AccessTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@FeignClient(name = "keycloakClient", url = "${keycloak.auth-server-url}")
public interface KeyCloakClient {

    @PostMapping(value = "/realms/{realm}/protocol/openid-connect/logout",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ResponseEntity<?> logout(
            @PathVariable("realm") String realm,
            Map<String, ?> formData
    );

    @PostMapping(value = "/realms/{realm}/protocol/openid-connect/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    AccessTokenResponse refreshToken(
            @PathVariable("realm") String realm,
            Map<String, ?> formData
    );
}
