package com.example.diploma.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidJwtTokenException extends AuthenticationException {

    public InvalidJwtTokenException() {
        super("Invalid JWT token");
    }

}