package com.kduytran.categoryservice.exception;

import com.kduytran.categoryservice.dto.ErrorResponseDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<ErrorResponseDTO> getErrorResponseEntity(
            Exception exception, WebRequest webRequest, boolean includeClientInfo, HttpStatus httpStatus) {
        ErrorResponseDTO errorResponseDto = new ErrorResponseDTO(
                webRequest.getDescription(includeClientInfo),
                httpStatus,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDto, httpStatus);
    }

    private ResponseEntity<ErrorResponseDTO> getErrorResponseEntity(
            Exception exception, WebRequest webRequest, HttpStatus httpStatus) {
        return this.getErrorResponseEntity(exception, webRequest, false, httpStatus);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();
        Map<String, String> validationErrors = validationErrorList.stream()
                .filter(error -> error instanceof FieldError)
                .map(error -> (FieldError) error)
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGlobalException(
            Exception exception, WebRequest webRequest) {
        return this.getErrorResponseEntity(exception, webRequest, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(
            ResourceNotFoundException exception, WebRequest webRequest) {
        return this.getErrorResponseEntity(exception, webRequest, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleCategoryNotFoundException(
            CategoryNotFoundException exception, WebRequest webRequest) {
        return this.getErrorResponseEntity(exception, webRequest, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleCategoryAlreadyExistsException(
            CategoryAlreadyExistsException exception, WebRequest webRequest) {
        return this.getErrorResponseEntity(exception, webRequest, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TooManyCategoryParentsException.class)
    public ResponseEntity<ErrorResponseDTO> handleTooManyCategoryParentsException(
            TooManyCategoryParentsException exception, WebRequest webRequest) {
        return this.getErrorResponseEntity(exception, webRequest, HttpStatus.NOT_FOUND);
    }

}
