package com.example.diploma.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = NullOrNotEmptyValidator.class)
public @interface NullOrNotEmpty{

    String message() default "Отчество должно быть либо пустым либо заполненным";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
