package com.example.polls.security.listener;

import com.example.polls.model.User;
import com.example.polls.payload.request.LoginRequest;
import com.example.polls.security.event.AuthenticationSuccessfulEvent;
import com.example.polls.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthenticationSuccessfulListener {

    private final UserService userService;

    @EventListener
    @Async
    public void onAuthenticationSuccess(AuthenticationSuccessfulEvent event) {
        LoginRequest loginRequest = event.getLoginRequest();
        String usernameOrEmail = loginRequest.getUsernameOrEmail();
        Optional<User> user = userService.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        user.ifPresent(this::recordSuccessAuthentication);
    }

    private void recordSuccessAuthentication(User user) {
        user.setLastLoginDate(new Date());
        userService.save(user);
    }
}
