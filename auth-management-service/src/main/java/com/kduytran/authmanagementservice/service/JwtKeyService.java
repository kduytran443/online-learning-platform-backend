package com.kduytran.authmanagementservice.service;

import com.kduytran.authmanagementservice.dto.JwtPairDTO;
import com.kduytran.authmanagementservice.entity.UserEntity;

import java.security.PublicKey;
import java.time.Duration;

public interface JwtKeyService {

    String getPublicKeyInBase64();

    String generateToken(UserEntity user, Duration expiry);

    String generateRefreshToken(UserEntity user, Duration expiry);

    PublicKey getPublicKey();

    JwtPairDTO getJwtPair(String username, Duration accessTokenDuration, Duration refreshTokenDuration);
}
