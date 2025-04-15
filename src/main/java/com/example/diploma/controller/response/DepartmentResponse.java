package com.example.diploma.controller.response;

import jakarta.validation.constraints.NotBlank;

public record DepartmentResponse(
        @NotBlank String name,
        FullnameResponse head
) {
}
