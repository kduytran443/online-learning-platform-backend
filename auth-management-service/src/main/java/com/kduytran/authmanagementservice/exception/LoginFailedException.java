package com.kduytran.authmanagementservice.exception;

public class LoginFailedException extends RuntimeException {
    public static final String USERNAME_OR_PASSWORD_INCORRECT = "Username or password is incorrect";

    public LoginFailedException(String message) {
        super(message);
    }
}
