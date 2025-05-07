package com.example.diploma.controller.request.vacation;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UpdateVacationRequest(
        @NotNull(message = "поле не должно быть пустым") @JsonFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
        @NotNull(message = "поле не должно быть пустым") @JsonFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
        String type
) {
}
