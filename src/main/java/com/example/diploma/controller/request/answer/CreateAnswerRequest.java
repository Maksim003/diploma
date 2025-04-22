package com.example.diploma.controller.request.answer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateAnswerRequest(
        @NotBlank String name,
        @NotNull Boolean isCorrect
) {
}
