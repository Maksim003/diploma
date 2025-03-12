package com.example.diploma.controller.request;

import com.example.diploma.annotation.NullOrNotEmpty;
import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(
       @NotBlank String name,
       @NotBlank String surname,
       @NullOrNotEmpty String patronymic,
       @NotBlank String login
) {
}
