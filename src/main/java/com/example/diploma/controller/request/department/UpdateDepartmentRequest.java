package com.example.diploma.controller.request.department;

import jakarta.validation.constraints.NotBlank;

public record UpdateDepartmentRequest(
        @NotBlank(message = "поле не должно быть пустым") String name,
        Long head
) {
}
