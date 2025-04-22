package com.example.diploma.exception;

public class BriefingResultNotFoundException extends RuntimeException {

    public BriefingResultNotFoundException() {
        super("Briefing result not found");
    }

}
