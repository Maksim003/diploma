package com.example.diploma.controller.response;

public record AnswerResponse(
        Long id,
        String name,
        Boolean isCorrect
) {
}
