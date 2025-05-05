package com.example.diploma.controller.request.user;

public record RegisterUserRequest(
        String surname,
        String name,
        String patronymic,
        String login,
        Long position
) {
}
