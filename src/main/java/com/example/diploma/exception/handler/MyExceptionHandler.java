package com.example.diploma.exception.handler;

import com.example.diploma.controller.response.ErrorResponse;
import com.example.diploma.controller.response.ValidResponse;
import com.example.diploma.exception.ExceptionEnum;
import com.example.diploma.exception.MyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidResponse> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        ValidResponse response = new ValidResponse(
                "VALIDATION_FAILED",
                "Ошибка валидации",
                errors);
        return ResponseEntity.badRequest().body(response);
    }

}
