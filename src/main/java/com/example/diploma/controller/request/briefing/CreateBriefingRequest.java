package com.example.diploma.controller.request.briefing;

import com.example.diploma.controller.request.question.CreateQuestionRequest;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateBriefingRequest(
        @NotNull(message = "поле не должно быть пустым") Long creator,
        String type,
        List<CreateQuestionRequest> questions
) {
}
