package com.example.diploma.controller.response;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record DepartmentResponse(
        @NotBlank String name,
        FullnameResponse head,
        List<FullnameResponse> members
) {
}
