package com.example.diploma.controller.request.appeal;

import jakarta.validation.constraints.NotNull;

public record CreateAppealRequest(
        @NotNull(message = "поле не должно быть пустым") Long user,
        String subject,
        String description,
        String status
) {
}
