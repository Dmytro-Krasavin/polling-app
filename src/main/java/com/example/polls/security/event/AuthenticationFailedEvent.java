package com.example.polls.security.event;

import com.example.polls.payload.request.LoginRequest;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AuthenticationFailedEvent extends ApplicationEvent {

    private final LoginRequest loginRequest;

    public AuthenticationFailedEvent(LoginRequest loginRequest) {
        super(loginRequest);
        this.loginRequest = loginRequest;
    }
}
