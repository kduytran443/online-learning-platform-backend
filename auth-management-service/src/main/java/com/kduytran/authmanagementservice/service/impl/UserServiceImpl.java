package com.kduytran.authmanagementservice.service.impl;

import com.kduytran.authmanagementservice.dto.UserDTO;
import com.kduytran.authmanagementservice.service.JwtKeyService;
import com.kduytran.authmanagementservice.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final JwtKeyService jwtKeyService;

    @Override
    public UserDTO getUserFromAccessToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(jwtKeyService.getPublicKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token", e);
        }

        return UserDTO.builder()
                .sub(claims.getSubject())
                .username(claims.get("username", String.class))
                .roles(claims.get("roles", List.class))
                .permissions(claims.get("permissions", List.class))
                .name(claims.get("name", String.class))
                .avatar(claims.get("avatar", String.class))
                .build();
    }
}
