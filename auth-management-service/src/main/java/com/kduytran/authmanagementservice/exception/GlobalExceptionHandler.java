package com.kduytran.authmanagementservice.exception;

import com.kduytran.authmanagementservice.dto.ErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(value = { LoginFailedException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponseDTO handleLoginFailedException(LoginFailedException ex, WebRequest webRequest) {
        log.warn(ex.getMessage(), ex);
        return ErrorResponseDTO.builder()
                .apiPath(webRequest.getDescription(false))
                .errorMessage(ex.getMessage())
                .errorCode(LoginFailedException.USERNAME_OR_PASSWORD_INCORRECT)
                .build();
    }

    @ExceptionHandler(value = {SignUpNotValidException.class, KeyCloakException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponseDTO handleBadRequestException(RuntimeException ex, WebRequest request) {
        log.error("Bad request: {}", ex.getMessage(), ex);
        return ErrorResponseDTO.builder()
                .apiPath(request.getDescription(false))
                .errorMessage(ex.getMessage())
                .errorTime(LocalDateTime.now())
                .errorCode(HttpStatus.BAD_REQUEST.toString())
                .build();
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorResponseDTO handleNotFoundException(RuntimeException ex, WebRequest request) {
        log.error("Not found: {}", ex.getMessage(), ex);
        return ErrorResponseDTO.builder()
                .apiPath(request.getDescription(false))
                .errorMessage(ex.getMessage())
                .errorTime(LocalDateTime.now())
                .errorCode(HttpStatus.NOT_FOUND.toString())
                .build();
    }
}
