package com.example.diploma.controller.response;

public record ErrorResponse(
        String code,
        String message
) {
}
