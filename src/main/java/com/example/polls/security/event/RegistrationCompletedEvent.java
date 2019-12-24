package com.example.polls.security.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.net.URI;

@Getter
public class RegistrationCompletedEvent extends ApplicationEvent {

    private final Long userId;
    private final URI confirmationUri;

    public RegistrationCompletedEvent(Long userId, URI confirmationUri) {
        super(userId);
        this.userId = userId;
        this.confirmationUri = confirmationUri;
    }
}
