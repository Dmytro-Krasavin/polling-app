package com.example.polls.security.event;

import com.example.polls.payload.request.LoginRequest;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OnAuthenticationSuccessEvent extends ApplicationEvent {

    private final LoginRequest loginRequest;

    public OnAuthenticationSuccessEvent(LoginRequest loginRequest) {
        super(loginRequest);
        this.loginRequest = loginRequest;
    }
}
