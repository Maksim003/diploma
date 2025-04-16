package com.example.diploma.exception;

public class DepartmentNotFoundException extends RuntimeException {

    public DepartmentNotFoundException() {
        super("Department Not Found");
    }

}
