package com.kduytran.classresourceservice.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;

@Primary
@FeignClient(name = "auth-management-service", primary = true, fallback = AuthServiceClientFallback.class)
public interface AuthServiceClient {

    @GetMapping("/api/v1/jwt-key/public-key")
    String getPublicKeyInBase64();
}
