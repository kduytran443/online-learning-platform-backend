package com.kduytran.paymentservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PayPalTransactionException extends RuntimeException {

    public PayPalTransactionException(String msg) {
        super(msg);
    }

}
