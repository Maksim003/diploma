package com.example.diploma.controller.request.incident;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record UpdateIncidentRequest(
        @NotNull(message = "поле не должно быть пустым") LocalDateTime date,
        String type,
        String description,
        String actions
) {
}
