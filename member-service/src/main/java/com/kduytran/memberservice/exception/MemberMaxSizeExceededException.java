package com.kduytran.memberservice.exception;

public class MemberMaxSizeExceededException extends RuntimeException {

    public MemberMaxSizeExceededException(String message, int maxSize) {
        super(String.format("%s. Maximum size allowed: %d.", message, maxSize));
    }
}
