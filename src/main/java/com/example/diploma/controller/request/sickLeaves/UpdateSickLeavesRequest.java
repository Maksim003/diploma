package com.example.diploma.controller.request.sickLeaves;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UpdateSickLeavesRequest(
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        String documentNumber,
        String status
) {
}
