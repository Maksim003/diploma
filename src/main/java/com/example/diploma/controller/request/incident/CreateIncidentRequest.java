package com.example.diploma.controller.request.incident;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateIncidentRequest(
        @NotNull(message = "поле не должно быть пустым") Long user,
        @NotNull(message = "поле не должно быть пустым") LocalDateTime date,
        String type,
        String description,
        String status
) {
}
