package com.kduytran.gatewayserver.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

@Component
public class FilterUtility {

    public static final String TRANSACTION_ID_KEY = "transaction-id";

    public String getTransactionId(HttpHeaders requestHeaders) {
        List<String> requestHeaderList = requestHeaders.get(TRANSACTION_ID_KEY);
        if (requestHeaderList != null && requestHeaderList.size() > 0) {
            return requestHeaderList.get(0);
        } else {
            return null;
        }
    }

    public ServerWebExchange setRequestHeader(ServerWebExchange exchange, String name, String value) {
        return exchange.mutate().request(exchange.getRequest().mutate().header(name, value).build()).build();
    }

    public ServerWebExchange setTransactionId(ServerWebExchange exchange, String transactionId) {
        return this.setRequestHeader(exchange, TRANSACTION_ID_KEY, transactionId);
    }

}
