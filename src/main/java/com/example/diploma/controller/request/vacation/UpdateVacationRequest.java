package com.example.diploma.controller.request.vacation;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UpdateVacationRequest(
        @NotNull(message = "поле не должно быть пустым") LocalDate startDate,
        @NotNull(message = "поле не должно быть пустым") LocalDate endDate,
        String type,
        String status
) {
}
