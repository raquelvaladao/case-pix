package com.example.demo.core.validators;

import com.example.demo.core.validators.annotations.NumberDigits;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MaxDigitsValidator implements ConstraintValidator<NumberDigits, String> {

    private int maxValue;

    @Override
    public boolean isValid(String input, ConstraintValidatorContext constraintValidatorContext) {
        if(!isNumeric(input))
            return false;

        return input.length() <= maxValue;
    }

    @Override
    public void initialize(NumberDigits constraintAnnotation) {
        this.maxValue = constraintAnnotation.max();
    }

    private static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        return str.matches("\\d+");
    }
}
