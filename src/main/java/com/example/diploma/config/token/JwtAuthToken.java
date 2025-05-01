package com.example.diploma.config.token;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

@Getter
public class JwtAuthToken extends AbstractAuthenticationToken {

    private final String principal;
    private final String token;

    public JwtAuthToken(String principal, String token) {
        super(null);
        this.principal = principal;
        this.token = token;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

}
