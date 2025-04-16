package com.example.diploma.exception;

public class AppealNotFoundException extends RuntimeException {

    public AppealNotFoundException() {
        super("Appeal Not Found");
    }

}
