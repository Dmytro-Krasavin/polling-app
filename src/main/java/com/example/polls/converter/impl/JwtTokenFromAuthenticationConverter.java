package com.example.polls.converter.impl;

import com.example.polls.converter.ModelConverter;
import com.example.polls.payload.JwtAuthenticationResponse;
import com.example.polls.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@RequiredArgsConstructor
public class JwtTokenFromAuthenticationConverter implements ModelConverter<Authentication, JwtAuthenticationResponse> {

    private final JwtTokenProvider tokenProvider;

    @Override
    public JwtAuthenticationResponse convert(Authentication authentication) {
        Assert.notNull(authentication, "Authentication must not be null!");
        String jwt = tokenProvider.generateToken(authentication);
        return new JwtAuthenticationResponse(jwt);
    }
}
