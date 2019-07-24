package com.example.polls.util.converter.impl;

import com.example.polls.util.converter.ModelConverter;
import com.example.polls.payload.response.JwtAuthenticationResponse;
import com.example.polls.security.service.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@RequiredArgsConstructor
public class AuthenticationToJwtTokenConverter implements ModelConverter<Authentication, JwtAuthenticationResponse> {

    private final TokenProvider tokenProvider;

    @Override
    public JwtAuthenticationResponse convert(Authentication authentication) {
        Assert.notNull(authentication, "Authentication must not be null!");
        String jwt = tokenProvider.generateToken(authentication);
        return new JwtAuthenticationResponse(jwt);
    }
}
