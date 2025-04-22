package com.example.diploma.controller.request.briefingResult;

import jakarta.validation.constraints.NotNull;

public record UpdateBriefingResultRequest(
        @NotNull Integer totalQuestions,
        @NotNull Integer correctAnswers,
        String status
) {
}
