package com.example.diploma.controller.response;

import java.time.LocalDateTime;

public record AppealResponse(
        FullnameResponse user,
        LocalDateTime date,
        String subject,
        String description,
        String answer
) {
}
