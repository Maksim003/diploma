package com.example.diploma.controller.request.briefingResult;

import jakarta.validation.constraints.NotNull;

public record CreateBriefingResultRequest(
        @NotNull Integer totalQuestions,
        @NotNull Integer correctAnswers,
        String status,
        @NotNull Long user,
        @NotNull Long briefing
) {
}
