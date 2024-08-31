package com.kduytran.orderservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPriceException extends RuntimeException {

    public InvalidPriceException(String msg) {
        super(msg);
    }

}
