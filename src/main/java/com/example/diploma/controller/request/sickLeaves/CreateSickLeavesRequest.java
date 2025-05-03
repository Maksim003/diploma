package com.example.diploma.controller.request.sickLeaves;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateSickLeavesRequest(
        @NotNull(message = "поле не должно быть пустым") Long user,
        @NotNull(message = "поле не должно быть пустым") @JsonFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
        @NotNull(message = "поле не должно быть пустым") @JsonFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
        String documentNumber,
        String status
) {
}
