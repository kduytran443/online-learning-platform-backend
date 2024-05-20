package com.kduytran.gatewayserver.config;

import com.kduytran.gatewayserver.utils.PathUtils;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path(PathUtils.getPathWithContextPath("{versioning:v\\d{1,2}}/categories/**"))
                        .filters(f -> f
                                .rewritePath(PathUtils.getPathWithContextPath("(?<versioning>v\\d{1,2})/categories/(?<segment>.*)"),
                                        "/api/${versioning}/categories/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                        )
                        .uri("lb://CATEGORY")
                )
                .build();
    }

}
