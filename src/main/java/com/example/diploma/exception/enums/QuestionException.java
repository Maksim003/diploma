package com.example.diploma.exception.enums;

import com.example.diploma.exception.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum QuestionException implements ExceptionEnum {

    NOT_FOUND("NOT_FOUND", "Вопрос не найден", HttpStatus.NOT_FOUND),
    ALREADY_EXISTS("ALREADY_EXISTS", "Вопрос уже существует", HttpStatus.CONFLICT),
    FORBIDDEN("FORBIDDEN", "Доступ запрещён", HttpStatus.FORBIDDEN);

    private final String code;
    private final String message;
    private final HttpStatus status;

}
