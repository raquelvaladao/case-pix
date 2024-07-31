package com.example.demo.core.validators.annotations;

import com.example.demo.core.validators.MaxDigitsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MaxDigitsValidator.class)
@Target( { ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface NumberDigits {
    String message() default "Invalid number size, max digits is {max} and numeric only";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    int max();
}