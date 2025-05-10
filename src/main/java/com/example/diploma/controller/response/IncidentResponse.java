package com.example.diploma.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record IncidentResponse(
        Long id,
        List<FullnameResponse> users,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime date,
        String type,
        String description,
        String actions
) {
}
