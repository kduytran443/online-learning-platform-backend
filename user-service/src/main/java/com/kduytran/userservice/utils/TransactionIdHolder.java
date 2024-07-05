package com.kduytran.userservice.utils;

import org.springframework.stereotype.Component;

@Component
public class TransactionIdHolder {
    public static final String HEADER_NAME = "transaction-id";
    private final ThreadLocal<String> currentTransactionId = new ThreadLocal<>();

    public String getCurrentTransactionId() {
        return currentTransactionId.get();
    }

    public void setCurrentTransactionId(String transactionId) {
        currentTransactionId.set(transactionId);
    }

}