package com.example.diploma.exception.enums;

import com.example.diploma.exception.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BriefingResultException implements ExceptionEnum {

    NOT_FOUND("NOT_FOUND", "Результат инструктажа не найден", HttpStatus.NOT_FOUND),
    ALREADY_EXISTS("ALREADY_EXISTS", "Результат инструктажа уже существует", HttpStatus.CONFLICT),
    FORBIDDEN("FORBIDDEN", "Доступ запрещён", HttpStatus.FORBIDDEN);

    private final String code;
    private final String message;
    private final HttpStatus status;

}
