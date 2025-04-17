package com.example.diploma.exception;

public class SickLeavesNotFoundException extends RuntimeException {

    public SickLeavesNotFoundException() {
        super("Sick leaves not found");
    }

}
