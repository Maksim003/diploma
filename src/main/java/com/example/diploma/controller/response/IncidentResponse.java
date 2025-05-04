package com.example.diploma.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record IncidentResponse(
        Long id,
        FullnameResponse user,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime date,
        String type,
        String description,
        String status
) {
}
