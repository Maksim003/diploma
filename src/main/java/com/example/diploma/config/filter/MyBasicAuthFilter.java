package com.example.diploma.config.filter;

import com.example.diploma.service.TokenService;
import com.example.diploma.util.SecurityUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.example.diploma.controller.response.TokenResponse;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MyBasicAuthFilter extends BasicAuthenticationFilter {

    private final TokenService tokenService;

    private final RequestMatcher requestMatcher = new AntPathRequestMatcher(SecurityUtil.AUTH_PATH);
    private final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

    public MyBasicAuthFilter(AuthenticationManager authenticationManager, TokenService tokenService) {
        super(authenticationManager);
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (requestMatcher.matches(request)) {
            super.doFilterInternal(request, response, filterChain);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    @Override
    protected void onSuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              Authentication authResult) throws IOException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        UserDetails principal = (UserDetails) authResult.getPrincipal();
        TokenResponse tokenResponse = tokenService.generateTokens(principal.getUsername());
        converter.write(tokenResponse, MediaType.APPLICATION_JSON, new ServletServerHttpResponse(response));
    }

}