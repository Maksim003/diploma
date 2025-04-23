package com.example.diploma.exception.handler;

import com.example.diploma.controller.response.ErrorResponse;
import com.example.diploma.exception.ExceptionEnum;
import com.example.diploma.exception.MyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(MyException.class)
    public ResponseEntity<ErrorResponse> handleUserException(MyException ex) {
        ExceptionEnum exceptionEnum = ex.getEx();
        ErrorResponse response = new ErrorResponse(
                exceptionEnum.getCode(),
                exceptionEnum.getMessage()
        );
        return ResponseEntity.status(exceptionEnum.getStatus()).body(response);
    }

}
