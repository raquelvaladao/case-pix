package com.example.demo.core.validators.annotations;

import com.example.demo.core.validators.SurnameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SurnameValidator.class)
@Target( { ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSurname {
    String message() default "Invalid holder surname, surpasses maximum characters";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}