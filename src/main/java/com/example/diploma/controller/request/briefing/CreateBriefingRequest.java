package com.example.diploma.controller.request.briefing;

import jakarta.validation.constraints.NotNull;

public record CreateBriefingRequest(
        @NotNull(message = "поле не должно быть пустым") Long creator,
        String type
) {
}
