package com.example.diploma.exception;

public class AnswerNotFoundException extends RuntimeException {

    public AnswerNotFoundException() {
        super("Answer Not Found");
    }

}
