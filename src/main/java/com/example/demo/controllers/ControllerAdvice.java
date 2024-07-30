package com.example.demo.controllers;


import com.example.demo.core.enums.ErrorMessage;
import com.example.demo.core.exceptions.BusinessException;
import com.example.demo.dtos.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorResponse>> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        var result = ex.getBindingResult();
        var fieldErrors = result.getFieldErrors();
        var errors = new ArrayList<ErrorResponse>();

        for (FieldError fieldError : fieldErrors) {
            errors.add(ErrorResponse.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .reason(ErrorMessage.INVALID_FIELD.name())
                    .build()
            );
        }
        return new ResponseEntity<>(errors, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<List<ErrorResponse>> businessException(BusinessException ex) {
        var errors = new ArrayList<ErrorResponse>() {{
            add(
                    ErrorResponse.builder()
                            .reason(ex.getCode().name())
                            .message(ex.getDetail())
                            .build()
            );
        }};
        return new ResponseEntity<>(errors, HttpStatus.valueOf(ex.getCode().getStatus()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<List<ErrorResponse>> runtimeException(RuntimeException ex) {
        log.info("Uncaught exception occurred: {}", ex.getMessage());

        var errors = new ArrayList<ErrorResponse>() {{
            add(
                    ErrorResponse.builder()
                            .reason(ErrorMessage.INTERNAL_ERROR.name())
                            .build()
            );
        }};
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<List<Object>> entityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
