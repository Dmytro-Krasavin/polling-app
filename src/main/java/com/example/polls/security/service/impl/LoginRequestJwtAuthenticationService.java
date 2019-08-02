package com.example.polls.security.service.impl;

import com.example.polls.payload.request.LoginRequest;
import com.example.polls.payload.response.JwtAuthenticationResponse;
import com.example.polls.security.service.AuthenticationService;
import com.example.polls.security.service.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginRequestJwtAuthenticationService implements AuthenticationService<LoginRequest, JwtAuthenticationResponse> {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @Override
    public JwtAuthenticationResponse authenticate(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsernameOrEmail(),
                loginRequest.getPassword()
        );
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return new JwtAuthenticationResponse(jwt);
    }
}
