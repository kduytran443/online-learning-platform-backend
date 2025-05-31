package com.kduytran.classresourceservice.service.client;

import org.springframework.stereotype.Component;

@Component
public class AuthServiceClientFallback implements AuthServiceClient {
    @Override
    public String getPublicKeyInBase64() {
        throw new RuntimeException("Failed to load public key");
    }
}
