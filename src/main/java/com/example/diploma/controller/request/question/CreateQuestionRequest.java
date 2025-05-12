package com.example.diploma.controller.request.question;

import com.example.diploma.controller.request.answer.CreateAnswerRequest;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CreateQuestionRequest(
        @NotBlank(message = "поле не должно быть пустым") String name,
        List<CreateAnswerRequest> answers
) {
}
