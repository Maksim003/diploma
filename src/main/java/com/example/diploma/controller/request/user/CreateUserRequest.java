package com.example.diploma.controller.request.user;

import com.example.diploma.annotation.NullOrNotEmpty;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
        @NotBlank String name,
        @NotBlank String surname,
        @NullOrNotEmpty String patronymic,
        @NotBlank String login,
        @NotBlank String password
) {
}
