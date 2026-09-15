package com.jimmy.portal.exception;

import com.jimmy.portal.base.ErrorResponseDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(Exception exception, WebRequest webRequest) {
        return ResponseEntity.internalServerError().body(new ErrorResponseDto(webRequest.getDescription(false), HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(
            MethodArgumentNotValidException exception,
            WebRequest webRequest) {

        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity.badRequest().body(
                new ErrorResponseDto(
                        webRequest.getDescription(false),
                        HttpStatus.BAD_REQUEST.value(),
                        message,
                        LocalDateTime.now()
                )
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleDataIntegrityViolation(
            DataIntegrityViolationException exception,
            WebRequest webRequest) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ErrorResponseDto(
                        webRequest.getDescription(false),
                        HttpStatus.CONFLICT.value(),
                        getDuplicateMessage(exception),
                        LocalDateTime.now()
                )
        );
    }

    private String getDuplicateMessage(DataIntegrityViolationException exception) {

        String message = exception.getMessage();

        if (message == null) {
            return "Data already exists.";
        }

        if (message.contains("users.email")) {
            return "Email already exists.";
        }

        if (message.contains("users.mobile_number")) {
            return "Mobile number already exists.";
        }

        return "Data already exists.";
    }
}