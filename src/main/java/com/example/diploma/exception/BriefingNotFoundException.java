package com.example.diploma.exception;

public class BriefingNotFoundException extends RuntimeException {

    public BriefingNotFoundException() {
        super("Briefing not found");
    }

}
