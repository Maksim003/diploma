package com.example.diploma.controller.request.briefing;

import jakarta.validation.constraints.NotNull;

public record CreateBriefingRequest(
        @NotNull Long creator,
        String type
) {
}
