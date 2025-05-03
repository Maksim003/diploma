package com.example.diploma.controller.request.appeal;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateAppealRequest(
        @NotNull(message = "поле не должно быть пустым") Long user,
        @NotNull(message = "поле не должно быть пустым") LocalDateTime date,
        String subject,
        String description
) {
}
