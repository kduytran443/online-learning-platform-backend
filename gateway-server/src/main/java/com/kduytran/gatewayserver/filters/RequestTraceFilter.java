package com.kduytran.gatewayserver.filters;

import com.kduytran.gatewayserver.utils.FilterUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Order(1)
@Component
public class RequestTraceFilter implements GlobalFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestTraceFilter.class);

    @Autowired
    private FilterUtility filterUtility;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
        if (isTransactionIdPresent(requestHeaders)) {
            logger.debug("Correlation-Id found in RequestTraceFilter : {}",
                    filterUtility.getCorrelationId(requestHeaders));
        } else {
            String correlationID = generateTransactionId();
            exchange = filterUtility.setCorrelationId(exchange, correlationID);
            logger.debug("Correlation-Id generated in RequestTraceFilter : {}", correlationID);
        }
        String path = exchange.getRequest().getPath().value();
        String destinationUri = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR).toString();
        logger.debug("Request Path: {}", path);
        logger.debug("Destination URI: {}", destinationUri);

        return chain.filter(exchange);
    }

    private boolean isTransactionIdPresent(HttpHeaders requestHeaders) {
        return filterUtility.getCorrelationId(requestHeaders) != null;
    }

    private String generateTransactionId() {
        return java.util.UUID.randomUUID().toString();
    }

}
