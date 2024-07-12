package com.kduytran.classqueryservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ClassAlreadyExistsException extends RuntimeException {

    public ClassAlreadyExistsException(String id) {
        super(String.format("Class with ID %s already exists.", id));
    }

}
