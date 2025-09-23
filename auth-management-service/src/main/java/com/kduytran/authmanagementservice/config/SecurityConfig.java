package com.kduytran.authmanagementservice.config;

import com.kduytran.authmanagementservice.handler.CustomAuthenticationFailureHandler;
import com.kduytran.authmanagementservice.handler.OAuth2LoginSuccessHandler;
import com.kduytran.authmanagementservice.service.CustomOAuth2UserService;
import com.kduytran.authmanagementservice.service.JwtKeyService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final JwtKeyService jwtKeyService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/auth/**", "/oauth2/**",
                                "/api/v1/jwt-key/**", "/api/v1/jwt-key/**",
                                "/login/oauth2/code/google", "/login-success", "/api/my-info"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Resource server for JWT API
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtConfigurer -> jwtConfigurer
                                .decoder(customJwtDecoder())
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                )

                // OAuth2 login used for web redirect
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService) // mapping oauth2 info to user in db
                        )
                        .successHandler(oAuth2LoginSuccessHandler) // generate token or redirect
                        .failureHandler(customAuthenticationFailureHandler)
                )

                // Default entry point for returning API JSON
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write("{\"error\": \"Unauthorized\"}");
                        })
                );
        return http.build();
    }

    @Bean
    public JwtDecoder customJwtDecoder() {
        RSAPublicKey publicKey = (RSAPublicKey) jwtKeyService.getPublicKey();
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    @Bean
    public Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");

        JwtAuthenticationConverter authConverter = new JwtAuthenticationConverter();

        authConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Collection<GrantedAuthority> authorities = new ArrayList<>(grantedAuthoritiesConverter.convert(jwt));

            Optional.ofNullable(jwt.getClaimAsStringList("permissions"))
                    .ifPresent(permissions -> permissions.forEach(p -> authorities.add(new SimpleGrantedAuthority(p))));

            return authorities;
        });

        return authConverter;
    }
}
