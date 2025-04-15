package com.kduytran.classresourceservice.service.impl;

import com.kduytran.classresourceservice.service.JwtKeyService;
import com.kduytran.classresourceservice.service.client.AuthServiceClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class JwtKeyServiceImpl implements JwtKeyService {

    private final AuthServiceClient authServiceClient;
    private PublicKey publicKey;

    @Override
    public PublicKey getPublicKey() {
        return publicKey;
    }

    @PostConstruct
    public void init() {
        this.publicKey = loadPublicKey();
    }

    private PublicKey loadPublicKey() {
        try {
            String key = authServiceClient.getPublicKeyInBase64();
            byte[] keyBytes = Base64.getDecoder().decode(key);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            return KeyFactory.getInstance("RSA").generatePublic(spec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load public key", e);
        }
    }
}
