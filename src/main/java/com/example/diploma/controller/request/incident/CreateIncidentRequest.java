package com.example.diploma.controller.request.incident;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateIncidentRequest(
        @NotNull Long user,
        @NotNull LocalDateTime date,
        String type,
        String description,
        String status
) {
}
