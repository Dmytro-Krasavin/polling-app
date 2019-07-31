package com.example.polls.security.event;

import com.example.polls.model.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.net.URI;

@Getter
public class RegistrationCompletedEvent extends ApplicationEvent {

    private final User user;
    private final URI confirmationUri;

    public RegistrationCompletedEvent(User user, URI confirmationUri) {
        super(user);
        this.user = user;
        this.confirmationUri = confirmationUri;
    }
}
