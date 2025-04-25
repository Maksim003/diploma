package com.example.diploma.controller.request.answer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateAnswerRequest(
        @NotBlank(message = "поле не должно быть пустым") String name,
        @NotNull(message = "поле не должно быть пустым") Boolean isCorrect
) {
}
