package com.example.diploma.controller.response;

import java.util.List;

public record BriefingResponse(
        Long id,
        FullnameResponse creator,
        String type,
        List<QuestionResponse> questions
) {
}
