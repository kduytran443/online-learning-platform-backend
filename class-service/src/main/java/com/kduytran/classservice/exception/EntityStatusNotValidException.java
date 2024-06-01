package com.kduytran.classservice.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EntityStatusNotValidException extends RuntimeException {

    public EntityStatusNotValidException(String status) {
        super(String.format("The provided status '%s' is not a valid value.", status));
    }

}
