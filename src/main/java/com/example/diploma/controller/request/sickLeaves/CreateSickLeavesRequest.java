package com.example.diploma.controller.request.sickLeaves;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateSickLeavesRequest(
        @NotNull Long user,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        String documentNumber,
        String status
) {
}
