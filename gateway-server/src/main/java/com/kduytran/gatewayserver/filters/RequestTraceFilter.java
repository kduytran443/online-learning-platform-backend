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
            logger.debug("transaction-id found in RequestTraceFilter : {}",
                    filterUtility.getTransactionId(requestHeaders));
        } else {
            String transactionID = generateTransactionId();
            exchange = filterUtility.setTransactionId(exchange, transactionID);
            logger.debug("transaction-id generated in RequestTraceFilter : {}", transactionID);
        }
        String path = exchange.getRequest().getPath().value();
        String destinationUri = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR).toString();
        logger.debug("Request Path: {}", path);
        logger.debug("Destination URI: {}", destinationUri);

        return chain.filter(exchange);
    }

    private boolean isTransactionIdPresent(HttpHeaders requestHeaders) {
        return filterUtility.getTransactionId(requestHeaders) != null;
    }

    private String generateTransactionId() {
        return java.util.UUID.randomUUID().toString();
    }

}
