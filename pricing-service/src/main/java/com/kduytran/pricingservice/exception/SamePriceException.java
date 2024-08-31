package com.kduytran.pricingservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SamePriceException extends RuntimeException {

    public SamePriceException(String msg) {
        super(msg);
    }

}
