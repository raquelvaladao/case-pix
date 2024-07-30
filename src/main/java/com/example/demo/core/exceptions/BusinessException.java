package com.example.demo.core.exceptions;

import com.example.demo.core.enums.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BusinessException extends RuntimeException {
    private ErrorMessage code;
    private String detail;
}
