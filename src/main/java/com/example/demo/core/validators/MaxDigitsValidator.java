package com.example.demo.core.validators;

import com.example.demo.core.validators.annotations.NumberDigits;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MaxDigitsValidator implements ConstraintValidator<NumberDigits, Integer> {

    private int maxValue;

    @Override
    public boolean isValid(Integer input, ConstraintValidatorContext constraintValidatorContext) {
        if (input == null) return false;

        return input.toString().length() <= maxValue;
    }

    @Override
    public void initialize(NumberDigits constraintAnnotation) {
        this.maxValue = constraintAnnotation.max();
    }
}
