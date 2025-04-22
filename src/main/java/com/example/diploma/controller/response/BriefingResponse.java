package com.example.diploma.controller.response;

import java.util.List;

public record BriefingResponse(
        FullnameResponse creator,
        String type,
        List<QuestionResponse> questions
) {
}
