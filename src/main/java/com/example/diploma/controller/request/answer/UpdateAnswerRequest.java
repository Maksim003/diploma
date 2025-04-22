package com.example.diploma.controller.request.answer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateAnswerRequest(
        @NotBlank String name,
        @NotNull Boolean isCorrect
) {
}
