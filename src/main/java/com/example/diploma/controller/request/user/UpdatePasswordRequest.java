package com.example.diploma.controller.request.user;

public record UpdatePasswordRequest(
        String oldPassword,
        String newPassword
) {
}
