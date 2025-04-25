package com.example.diploma.controller.request.appeal;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateAppealRequest(
        @NotNull(message = "поле не должно быть пустым") Long user,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
        String subject,
        String description
) {
}
