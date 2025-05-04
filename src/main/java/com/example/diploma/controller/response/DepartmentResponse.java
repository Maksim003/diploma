package com.example.diploma.controller.response;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record DepartmentResponse(
        Long id,
        @NotBlank String name,
        Long head,
        List<FullnameResponse> members
) {
}
