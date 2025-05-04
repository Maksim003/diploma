package com.example.diploma.controller.response;

public record CurrentUser(
        Long id,
        String username,
        String name,
        String surname,
        String patronymic,
        String fullName,
        String role,
        Long departmentId
) {
}
