package com.example.diploma.controller.response;

import java.util.List;

public record TokenResponse(String accessToken,
                            String refreshToken
) {
    public TokenResponse(List<String> tokens) {
        this(tokens.get(0), tokens.get(1));
    }
}
