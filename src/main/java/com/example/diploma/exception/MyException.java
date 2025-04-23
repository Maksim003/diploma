package com.example.diploma.exception;

import lombok.Getter;

@Getter
public class MyException extends RuntimeException {

    private final ExceptionEnum ex;

    public MyException(ExceptionEnum ex) {
        this.ex = ex;
    }

}
