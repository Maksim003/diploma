package com.example.diploma.controller.request.user;

import com.example.diploma.annotation.NullOrNotEmpty;
import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(
       @NotBlank(message = "поле не должно быть пустым") String name,
       @NotBlank(message = "поле не должно быть пустым") String surname,
       @NullOrNotEmpty String patronymic,
       @NotBlank(message = "поле не должно быть пустым") String login,
       Long role
) {
}
