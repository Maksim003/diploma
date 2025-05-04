package com.example.diploma.controller.response;

import java.util.List;

public record QuestionResponse(
        Long id,
        String name,
        List<AnswerResponse> answers
) {
}
