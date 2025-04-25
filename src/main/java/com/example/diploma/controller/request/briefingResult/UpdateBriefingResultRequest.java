package com.example.diploma.controller.request.briefingResult;

import jakarta.validation.constraints.NotNull;

public record UpdateBriefingResultRequest(
        @NotNull(message = "поле не должно быть пустым") Integer totalQuestions,
        @NotNull(message = "поле не должно быть пустым") Integer correctAnswers,
        String status
) {
}
