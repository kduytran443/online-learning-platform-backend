package com.kduytran.authmanagementservice.service.impl;

import com.kduytran.authmanagementservice.entity.PermissionEntity;
import com.kduytran.authmanagementservice.entity.RoleEntity;
import com.kduytran.authmanagementservice.entity.UserEntity;
import com.kduytran.authmanagementservice.repository.PermissionRepository;
import com.kduytran.authmanagementservice.repository.RoleRepository;
import com.kduytran.authmanagementservice.service.JwtKeyService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class JwtKeyServiceImpl implements JwtKeyService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Value("${olp.jwt.public-key-path}")
    private String publicKeyPath;

    @Value("${olp.jwt.private-key-path}")
    private String privateKeyPath;

    private PublicKey publicKey;

    private PrivateKey privateKey;

    @Override
    public String getPublicKeyInBase64() {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    @Override
    public String generateToken(UserEntity user, Duration expiry) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("username", user.getUsername())
                .claim("roles", getRoles(user))
                .claim("permissions", getPermissions(user))
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(expiry)))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    @Override
    public String generateRefreshToken(UserEntity user, Duration expiry) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("token_type", "refresh")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(expiry)))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    private List<String> getRoles(UserEntity user) {
        return user.getRoles()
                .stream()
                .map(RoleEntity::getName)
                .toList();
    }

    private List<String> getPermissions(UserEntity user) {
        return user.getRoles()
                .stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(PermissionEntity::getName)
                .toList();
    }

    @PostConstruct
    public void init() {
        this.publicKey = loadPublicKey(publicKeyPath);
        this.privateKey = loadPrivateKey(privateKeyPath);
    }

    private PublicKey loadPublicKey(String path) {
        try (InputStream in = new FileInputStream(path)) {
            String key = getKey(in.readAllBytes());
            byte[] keyBytes = Base64.getDecoder().decode(key);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            return KeyFactory.getInstance("RSA").generatePublic(spec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load public key", e);
        }
    }

    private PrivateKey loadPrivateKey(String path) {
        try (InputStream in = new FileInputStream(path)) {
            String key = getKey(in.readAllBytes());
            byte[] keyBytes = Base64.getDecoder().decode(key);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            return KeyFactory.getInstance("RSA").generatePrivate(spec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load private key", e);
        }
    }

    private String getKey(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8)
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");
    }
}
