package com.kduytran.gatewayserver.config;

import jakarta.ws.rs.HttpMethod;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@EnableWebFluxSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
        serverHttpSecurity.authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/**").permitAll());
        serverHttpSecurity.csrf(ServerHttpSecurity.CsrfSpec::disable);
        return serverHttpSecurity.build();
    }

    @Bean
    CorsConfigurationSource corsConfiguration(@Value("${olp.front-end-urls}") List<String> allowedOrigins) {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.applyPermitDefaultValues();
        corsConfig.addAllowedMethod(HttpMethod.POST);
        corsConfig.addAllowedMethod(HttpMethod.PUT);
        corsConfig.addAllowedMethod(HttpMethod.PATCH);
        corsConfig.addAllowedMethod(HttpMethod.DELETE);
        corsConfig.addAllowedMethod(HttpMethod.GET);
        corsConfig.addAllowedMethod(HttpMethod.OPTIONS);
        corsConfig.addAllowedMethod(HttpMethod.HEAD);
        corsConfig.setAllowedOrigins(allowedOrigins);
        corsConfig.setAllowCredentials(true);
        corsConfig.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }

}
