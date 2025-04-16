package com.example.diploma.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record VacationResponse(
        FullnameResponse user,
        @JsonFormat(pattern = "yyyy-MM-dd")LocalDate startDate,
        @JsonFormat(pattern = "yyyy-MM-dd")LocalDate endDate,
        String type,
        String status,
        FullnameResponse approver
) {
}
