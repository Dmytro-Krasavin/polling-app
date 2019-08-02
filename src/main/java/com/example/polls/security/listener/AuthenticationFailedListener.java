package com.example.polls.security.listener;

import com.example.polls.security.event.AuthenticationFailedEvent;
import com.example.polls.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationFailedListener {

    private final UserService userService;

    @EventListener
    @Async
    public void onAuthenticationFail(AuthenticationFailedEvent event) {
        String usernameOrEmail = event.getLoginRequest().getUsernameOrEmail();
        userService.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .ifPresent(userService::recordFailedAuthenticationAttempt);
    }
}
