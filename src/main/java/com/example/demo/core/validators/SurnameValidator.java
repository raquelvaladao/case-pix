package com.example.demo.core.validators;

import com.example.demo.core.validators.annotations.ValidSurname;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.logging.log4j.util.Strings;

public class SurnameValidator implements ConstraintValidator<ValidSurname, String> {

    @Override
    public boolean isValid(String input, ConstraintValidatorContext constraintValidatorContext) {
        if (Strings.isBlank(input))
            return true;

        return input.length() <= 30;
    }
    
}
