package com.example.diploma.controller.request.department;

import jakarta.validation.constraints.NotBlank;

public record UpdateDepartmentRequest(
        @NotBlank String name,
        Long head
) {
}
