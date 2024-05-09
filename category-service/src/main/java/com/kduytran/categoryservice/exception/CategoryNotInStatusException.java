package com.kduytran.categoryservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CategoryNotInStatusException extends RuntimeException {

    public CategoryNotInStatusException(String message) {
        super(message);
    }

}
