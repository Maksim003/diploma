package com.example.diploma.controller.request.department;

import jakarta.validation.constraints.NotBlank;

public record CreateDepartmentRequest(
        @NotBlank String name,
        Long head
) {
}
