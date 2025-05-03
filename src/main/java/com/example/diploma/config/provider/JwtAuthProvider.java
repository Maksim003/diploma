package com.example.diploma.config.provider;

import com.example.diploma.config.token.JwtAuthToken;
import com.example.diploma.exception.InvalidJwtTokenException;
import com.example.diploma.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthProvider implements AuthenticationProvider {

    private final TokenService tokenService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthToken jwtAuthToken = (JwtAuthToken) authentication;
        String jwt = jwtAuthToken.getToken();
        String username = jwtAuthToken.getPrincipal();

        if (tokenService.validateToken(jwt, username)) {
            return jwtAuthToken;
        } else {
            throw new InvalidJwtTokenException();
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthToken.class.isAssignableFrom(authentication);
    }

}