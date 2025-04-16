package com.example.diploma.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record AppealResponse(
        FullnameResponse user,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
        String subject,
        String description,
        String answer
) {
}
