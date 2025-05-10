package com.example.diploma.controller.request.incident;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record CreateIncidentRequest(
        @NotNull(message = "поле не должно быть пустым") List<Long> users,
        @NotNull(message = "поле не должно быть пустым") LocalDateTime date,
        String type,
        String description,
        String actions
) {
}
