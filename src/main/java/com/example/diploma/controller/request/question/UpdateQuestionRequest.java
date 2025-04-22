package com.example.diploma.controller.request.question;

import jakarta.validation.constraints.NotBlank;

public record UpdateQuestionRequest(
        @NotBlank String name
) {
}
