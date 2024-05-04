package com.kduytran.categoryservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class TooManyCategoryParentsException extends RuntimeException {

    public TooManyCategoryParentsException(String message) {
        super(message);
    }

}
