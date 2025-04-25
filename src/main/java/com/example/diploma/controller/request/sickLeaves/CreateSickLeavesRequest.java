package com.example.diploma.controller.request.sickLeaves;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateSickLeavesRequest(
        @NotNull(message = "поле не должно быть пустым") Long user,
        @NotNull(message = "поле не должно быть пустым") LocalDate startDate,
        @NotNull(message = "поле не должно быть пустым") LocalDate endDate,
        String documentNumber,
        String status
) {
}
