package com.example.diploma.controller.request.vacation;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateVacationRequest(
        @NotNull Long user,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        String type,
        String status
) {
}
