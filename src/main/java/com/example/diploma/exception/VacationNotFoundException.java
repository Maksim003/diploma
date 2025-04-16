package com.example.diploma.exception;

public class VacationNotFoundException extends RuntimeException {

    public VacationNotFoundException() {
        super("Vacation Not Found");
    }

}
