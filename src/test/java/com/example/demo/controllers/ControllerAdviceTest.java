package com.example.demo.controllers;

import com.example.demo.core.enums.ErrorMessage;
import com.example.demo.core.exceptions.BusinessException;
import com.example.demo.dtos.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ControllerAdviceTest {

    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;

    @InjectMocks
    private ControllerAdvice controllerAdvice;

    @Test
    void methodArgumentNotValidException() {
        var bindingResult = mock(BindingResult.class);
        var fieldErrors = List.of(
                new FieldError("surname", "fieldName", "Error surname")
        );

        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        ResponseEntity<List<ErrorResponse>> response = controllerAdvice.methodArgumentNotValidException(methodArgumentNotValidException);

        assertEquals(ErrorMessage.INVALID_FIELD.name(), Objects.requireNonNull(response.getBody()).get(0).getReason());
        assertEquals("fieldName", Objects.requireNonNull(response.getBody()).get(0).getField());
        assertEquals("Error surname", Objects.requireNonNull(response.getBody()).get(0).getMessage());
    }

    @Test
    void businessException() {
        BusinessException businessException = new BusinessException(ErrorMessage.INACTIVE_KEY, "Inactive key");

        ResponseEntity<List<ErrorResponse>> response = controllerAdvice.businessException(businessException);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    void runtimeException() {
        ResponseEntity<List<ErrorResponse>> test = controllerAdvice.runtimeException(new RuntimeException("Test"));

        assertEquals(ErrorMessage.INTERNAL_ERROR.name(), Objects.requireNonNull(test.getBody()).get(0).getReason());
    }

    @Test
    void entityNotFoundException() {
        ResponseEntity<List<Object>> test = controllerAdvice.entityNotFoundException(new EntityNotFoundException());

        assertEquals(test.getStatusCode(), HttpStatusCode.valueOf(404));
    }
}