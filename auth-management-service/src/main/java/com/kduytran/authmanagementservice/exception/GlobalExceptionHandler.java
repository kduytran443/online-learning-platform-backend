package com.kduytran.authmanagementservice.exception;

import com.kduytran.authmanagementservice.dto.ErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

@Slf4j
@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(value = {SignUpNotValidException.class})
    ErrorResponseDTO handleBadRequestException(RuntimeException ex, WebRequest request) {
        log.error("Bad request: {}", ex.getMessage(), ex);
        return makeResponse(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    ErrorResponseDTO handleNotFoundException(RuntimeException ex, WebRequest request) {
        log.error("Not found: {}", ex.getMessage(), ex);
        return makeResponse(ex, request, HttpStatus.NOT_FOUND);
    }

    private ErrorResponseDTO makeResponse(Exception ex, WebRequest request, HttpStatus status) {
        return ErrorResponseDTO.builder()
                .apiPath(request.getDescription(false))
                .errorMessage(ex.getMessage())
                .errorTime(Instant.now())
                .errorCode(status)
                .build();
    }
}
