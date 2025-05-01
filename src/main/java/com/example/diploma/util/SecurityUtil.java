package com.example.diploma.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityUtil {

    public static final String AUTH_PATH = "/auth";
    public static final String REFRESH_PATH = "/refresh";
    public static final String LOGOUT_PATH = "/logout";

    public static final String  BEARER_TOKEN = "Bearer";

    @Value("${security.password-encode-strength}")
    private int encodingStrength;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(encodingStrength);
    }

}
