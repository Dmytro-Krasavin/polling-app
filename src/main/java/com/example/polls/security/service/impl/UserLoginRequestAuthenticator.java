package com.example.polls.security.service.impl;

import com.example.polls.payload.request.LoginRequest;
import com.example.polls.security.service.UserAuthenticator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLoginRequestAuthenticator implements UserAuthenticator<LoginRequest> {

    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication authenticate(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsernameOrEmail(),
                loginRequest.getPassword()
        );
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }
}
