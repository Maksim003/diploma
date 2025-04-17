package com.example.diploma.exception;

public class IncidentNotFoundException extends RuntimeException {

    public IncidentNotFoundException() {
        super("Incident not found");
    }

}

