package com.example.diploma.controller.response;

public record FullnameResponse(
        Long id,
        String fullName,
        String position,
        Long departmentId,
        String departmentName
) {
}
