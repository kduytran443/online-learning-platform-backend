package com.kduytran.classresourceservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CannotMoveException extends RuntimeException {

    public CannotMoveException(String msg) {
        super(msg);
    }

}
