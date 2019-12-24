package com.example.polls.security.service.impl.decorator;

import com.example.polls.payload.request.LoginRequest;
import com.example.polls.payload.request.SignUpRequest;
import com.example.polls.payload.response.JwtAuthenticationResponse;
import com.example.polls.payload.response.UserResponse;
import com.example.polls.security.event.RegistrationCompletedEvent;
import com.example.polls.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Service
@Primary
@RequiredArgsConstructor
public class EventPublishingAuthenticationService implements AuthenticationService {

    private final AuthenticationService authenticationService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest) {
        return authenticationService.authenticateUser(loginRequest);
    }

    @Override
    public UserResponse registerUser(SignUpRequest signUpRequest) {
        UserResponse userResponse = authenticationService.registerUser(signUpRequest);

        URI confirmationUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/auth/verifyEmail").build().toUri();
        eventPublisher.publishEvent(new RegistrationCompletedEvent(userResponse.getId(), confirmationUri));
        return userResponse;
    }
}
