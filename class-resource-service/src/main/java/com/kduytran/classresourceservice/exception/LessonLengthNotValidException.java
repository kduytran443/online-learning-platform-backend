package com.kduytran.classresourceservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LessonLengthNotValidException extends RuntimeException {

    public LessonLengthNotValidException(String msg) {
        super(msg);
    }

}
