package com.example.polls.converter.impl;

import com.example.polls.converter.ModelConverter;
import com.example.polls.payload.JwtAuthenticationResponse;
import com.example.polls.payload.LoginRequest;
import com.example.polls.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationResponseFromLoginRequestConverter implements ModelConverter<LoginRequest, JwtAuthenticationResponse> {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider tokenProvider;

    @Autowired
    public JwtAuthenticationResponseFromLoginRequestConverter(AuthenticationManager authenticationManager,
                                                              JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public JwtAuthenticationResponse convert(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        return new JwtAuthenticationResponse(jwt);
    }
}