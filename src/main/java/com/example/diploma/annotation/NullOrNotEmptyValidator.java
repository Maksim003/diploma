package com.example.diploma.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NullOrNotEmptyValidator implements ConstraintValidator<NullOrNotEmpty, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s == null || !s.isEmpty();
    }
}
