package com.example.diploma.controller.response;

public record BriefingResultResponse(
        Long id,
        Integer totalQuestions,
        Integer correctAnswers,
        Double percentage,
        String status,
        FullnameResponse user,
        ShortBriefingResponse briefing
) {
}
