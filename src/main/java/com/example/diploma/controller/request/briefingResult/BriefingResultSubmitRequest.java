package com.example.diploma.controller.request.briefingResult;

import java.util.List;

public record BriefingResultSubmitRequest(
        Long briefingId,
        List<BriefingAnswerSubmission> answers,
        Long userId
) {
}
