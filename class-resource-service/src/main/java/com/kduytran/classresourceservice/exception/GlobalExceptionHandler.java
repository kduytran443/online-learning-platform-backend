package com.kduytran.classresourceservice.exception;

import com.kduytran.classresourceservice.dto.ErrorResponseDTO;
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGlobalException(Exception exception, WebRequest webRequest) {
        return getErrorResponseEntity(exception, webRequest, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(
            ResourceNotFoundException exception,
            WebRequest webRequest) {
        return getErrorResponseEntity(exception, webRequest, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TopicCannotMoveException.class)
    public ResponseEntity<ErrorResponseDTO> handleSameTopicException(
            TopicCannotMoveException exception,
            WebRequest webRequest) {
        return getErrorResponseEntity(exception, webRequest, HttpStatus.BAD_REQUEST);
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

    private ResponseEntity<ErrorResponseDTO> getErrorResponseEntity(
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
