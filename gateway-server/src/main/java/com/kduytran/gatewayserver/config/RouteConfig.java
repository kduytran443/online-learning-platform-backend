package com.kduytran.gatewayserver.config;

import com.kduytran.gatewayserver.constant.ServiceConstant;
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
                        .path(PathUtils.getServicePath(ServiceConstant.CATEGORY_CONTEXT_PATH))
                        .filters(f -> f
                                .rewritePath(PathUtils.getRewriteSourcePath(ServiceConstant.CATEGORY_CONTEXT_PATH),
                                        PathUtils.getRewriteDestinationPath(ServiceConstant.CATEGORY_CONTEXT_PATH))
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                        )
                        .uri(PathUtils.getUri(ServiceConstant.CATEGORY_NAME))
                )
                .build();
    }

}
