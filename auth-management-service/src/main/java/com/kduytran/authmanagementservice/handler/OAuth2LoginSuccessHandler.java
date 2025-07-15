package com.kduytran.authmanagementservice.handler;

import com.kduytran.authmanagementservice.entity.UserEntity;
import com.kduytran.authmanagementservice.exception.ResourceNotFoundException;
import com.kduytran.authmanagementservice.repository.UserRepository;
import com.kduytran.authmanagementservice.service.JwtKeyService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;

    private final JwtKeyService jwtKeyService;

    @Value("${olp.secured-token}")
    private String securedToken;

    @Value("${olp.jwt.token-exp}")
    private String tokenExp;

    @Value("${olp.jwt.refresh-token-exp}")
    private String refreshTokenExp;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName();
        UserEntity user = userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("User with username [%s] not found".formatted(username)));

        response.addCookie(makeAccessTokenCookie(user));
        response.addCookie(makeRefreshTokenCookie(user));
        response.sendRedirect("/login-success");
    }

    private Cookie makeAccessTokenCookie(UserEntity user) {
        Duration duration = Duration.parse(tokenExp);
        String token = jwtKeyService.generateToken(user, duration);
        return makeCookie("accessToken", token, duration.getSeconds());
    }

    private Cookie makeRefreshTokenCookie(UserEntity user) {
        Duration duration = Duration.parse(refreshTokenExp);
        String refreshToken = jwtKeyService.generateRefreshToken(user, duration);
        return makeCookie("refreshToken", refreshToken, duration.getSeconds());
    }

    private Cookie makeCookie(String name, String value, long maxAge) {
        Cookie accessTokenCookie = new Cookie(name, value);
        accessTokenCookie.setSecure(Boolean.parseBoolean(securedToken)); // true in production
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge((int) maxAge);
        return accessTokenCookie;
    }
}
