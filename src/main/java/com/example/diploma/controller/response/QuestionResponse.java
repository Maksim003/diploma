package com.example.diploma.controller.response;

import java.util.List;

public record QuestionResponse(
        String name,
        List<AnswerResponse> answers
) {
}
