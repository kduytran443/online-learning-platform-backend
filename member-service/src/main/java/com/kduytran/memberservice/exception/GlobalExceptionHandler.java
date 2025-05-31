package com.kduytran.memberservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception exception, WebRequest webRequest) {
        return getErrorResponseEntity(exception, webRequest, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException exception,
            WebRequest webRequest) {
        return getErrorResponseEntity(exception, webRequest, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleClassAlreadyExistsException(
            ResourceAlreadyExistsException exception,
            WebRequest webRequest) {
        return getErrorResponseEntity(exception, webRequest, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorResponse> getErrorResponseEntity(
            Exception exception, WebRequest webRequest, HttpStatus httpStatus) {
        ErrorResponse errorResponse = new ErrorResponse(
                webRequest.getDescription(false),
                httpStatus,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
}
