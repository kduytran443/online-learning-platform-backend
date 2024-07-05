package com.kduytran.gatewayserver.filters;

import com.kduytran.gatewayserver.utils.FilterUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

@Configuration
public class ResponseTraceFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestTraceFilter.class);

    @Autowired
    private FilterUtility filterUtility;

    @Bean
    public GlobalFilter postGlobalFilter() {
        return (exchange, chain) -> chain.filter(exchange).then(Mono.fromRunnable(() -> {
            HttpHeaders httpHeaders = exchange.getResponse().getHeaders();
            String transactionId = filterUtility.getTransactionId(httpHeaders);
            logger.debug("Updated the transaction id to the outbound headers: {}", transactionId);
            exchange.getResponse().getHeaders().add(filterUtility.TRANSACTION_ID_KEY, transactionId);
        }));
    }

}
