package com.example.diploma.exception;

import org.springframework.http.HttpStatus;

public interface ExceptionEnum {

    String getCode();
    String getMessage();
    HttpStatus getStatus();

}
