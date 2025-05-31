package com.kduytran.memberservice.exception;

public class MemberSizeUnderflowException extends RuntimeException {
    public MemberSizeUnderflowException(String message) {
        super(message);
    }
}
