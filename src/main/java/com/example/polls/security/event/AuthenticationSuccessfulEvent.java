package com.example.polls.security.event;

import com.example.polls.payload.request.LoginRequest;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AuthenticationSuccessfulEvent extends ApplicationEvent {

    private final LoginRequest loginRequest;

    public AuthenticationSuccessfulEvent(LoginRequest loginRequest) {
        super(loginRequest);
        this.loginRequest = loginRequest;
    }
}
