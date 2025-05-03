package com.example.diploma.controller.response;

public record CurrentUser(
        String username,
        String name,
        String surname,
        String patronymic,
        String role
) {
}
