package com.example.polls.security.service.impl.decorator;

import com.example.polls.payload.request.LoginRequest;
import com.example.polls.payload.response.JwtAuthenticationResponse;
import com.example.polls.security.event.AuthenticationFailedEvent;
import com.example.polls.security.event.AuthenticationSuccessfulEvent;
import com.example.polls.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

@Component
@Primary
@RequiredArgsConstructor
public class EventPublishableAuthenticationService implements AuthenticationService<LoginRequest, JwtAuthenticationResponse> {

    private final AuthenticationService<LoginRequest, JwtAuthenticationResponse> authenticationService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public JwtAuthenticationResponse authenticate(LoginRequest loginRequest) {
        try {
            JwtAuthenticationResponse jwtResponse = authenticationService.authenticate(loginRequest);
            eventPublisher.publishEvent(new AuthenticationSuccessfulEvent(loginRequest));
            return jwtResponse;
        } catch (BadCredentialsException e) {
            eventPublisher.publishEvent(new AuthenticationFailedEvent(loginRequest));
            throw e;
        }
    }
}
