package com.example.polls.security.event;

import com.example.polls.model.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.net.URL;

@Getter
public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private final User user;
    private final URL url;

    public OnRegistrationCompleteEvent(User user, URL url) {
        super(user);
        this.user = user;
        this.url = url;
    }
}
