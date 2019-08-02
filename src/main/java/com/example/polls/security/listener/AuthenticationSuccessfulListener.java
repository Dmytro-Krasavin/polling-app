package com.example.polls.security.listener;

import com.example.polls.security.event.AuthenticationSuccessfulEvent;
import com.example.polls.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationSuccessfulListener {

    private final UserService userService;

    @EventListener
    @Async
    public void onAuthenticationSuccess(AuthenticationSuccessfulEvent event) {
        String usernameOrEmail = event.getLoginRequest().getUsernameOrEmail();
        userService.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .ifPresent(userService::recordSuccessAuthentication);
    }
}
