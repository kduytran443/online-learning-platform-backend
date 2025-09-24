package com.kduytran.authmanagementservice.handler;

import com.kduytran.authmanagementservice.dto.JwtPairDTO;
import com.kduytran.authmanagementservice.service.JwtKeyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;

import static com.kduytran.authmanagementservice.constant.ApiPathConstant.V1_LOGIN_SUCCESS;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtKeyService jwtKeyService;

    @Value("${olp.secured-token}")
    private String securedToken;

    @Value("${olp.jwt.token-exp}")
    private String tokenExp;

    @Value("${olp.jwt.refresh-token-exp}")
    private String refreshTokenExp;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String username = authentication.getName();
        Duration accessTokenDuration = Duration.parse(tokenExp);
        Duration refreshTokenDuration = Duration.parse(refreshTokenExp);
        JwtPairDTO jwtPair = jwtKeyService.getJwtPair(username, accessTokenDuration, refreshTokenDuration);

        response.addHeader(HttpHeaders.SET_COOKIE,
                makeCookie("accessToken", jwtPair.accessToken(), accessTokenDuration.getSeconds()).toString());
        response.addHeader(HttpHeaders.SET_COOKIE,
                makeCookie("refreshToken", jwtPair.refreshToken(), refreshTokenDuration.getSeconds()).toString());

        response.sendRedirect(V1_LOGIN_SUCCESS);
    }

    private ResponseCookie makeCookie(String name, String value, long maxAge) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(Boolean.parseBoolean(securedToken)) // true in production
                .path("/")
                .maxAge(maxAge)
                .sameSite("Lax")
                .build();
    }
}
