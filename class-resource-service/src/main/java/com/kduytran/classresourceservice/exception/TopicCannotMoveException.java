package com.kduytran.classresourceservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TopicCannotMoveException extends RuntimeException {

    public TopicCannotMoveException(String msg) {
        super(msg);
    }

}
