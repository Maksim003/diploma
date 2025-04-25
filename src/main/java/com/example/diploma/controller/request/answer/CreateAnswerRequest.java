package com.example.diploma.controller.request.answer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateAnswerRequest(
        @NotBlank(message = "Ответ не должен быть пустым") String name,
        @NotNull(message = "поле не должно быть пустым") Boolean isCorrect
) {
}
