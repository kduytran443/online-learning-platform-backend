package com.kduytran.authmanagementservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class KeyCloakActionFailedException extends RuntimeException {

    public KeyCloakActionFailedException(String msg) {
        super(msg);
    }

}
