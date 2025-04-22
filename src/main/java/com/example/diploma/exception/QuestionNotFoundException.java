package com.example.diploma.exception;

public class QuestionNotFoundException extends RuntimeException {

    public QuestionNotFoundException() {
        super("Question not found");
    }

}
