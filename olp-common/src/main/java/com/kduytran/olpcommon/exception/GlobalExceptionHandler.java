package com.kduytran.olpcommon.exception;

import com.kduytran.olpcommon.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponseDTO> handleGlobalException(Exception exception, WebRequest webRequest) {
        return getErrorResponseEntity(exception, webRequest, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(
            ResourceNotFoundException exception,
            WebRequest webRequest) {
        return getErrorResponseEntity(exception, webRequest, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExistedException.class)
    protected ResponseEntity<ErrorResponseDTO> handleResourceAlreadyExistedException(
            ResourceAlreadyExistedException exception,
            WebRequest webRequest) {
        return getErrorResponseEntity(exception, webRequest, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();
        Map<String, String> validationErrors = validationErrorList.stream()
                .filter(error -> error instanceof FieldError)
                .map(error -> (FieldError) error)
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }

    protected ResponseEntity<ErrorResponseDTO> getErrorResponseEntity(
            Exception exception, WebRequest webRequest, HttpStatus httpStatus) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                webRequest.getDescription(false),
                httpStatus,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(httpStatus).body(errorResponseDTO);
    }
}
