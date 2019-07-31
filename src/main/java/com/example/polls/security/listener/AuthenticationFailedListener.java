package com.example.polls.security.listener;

import com.example.polls.model.User;
import com.example.polls.payload.request.LoginRequest;
import com.example.polls.security.event.AuthenticationFailedEvent;
import com.example.polls.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthenticationFailedListener {

    private final UserService userService;

    @EventListener
    @Async
    public void onAuthenticationFail(AuthenticationFailedEvent event) {
        LoginRequest loginRequest = event.getLoginRequest();
        String usernameOrEmail = loginRequest.getUsernameOrEmail();
        Optional<User> user = userService.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        user.ifPresent(this::recordFailedAuthenticationAttempt);
    }

    private void recordFailedAuthenticationAttempt(User user) {
        int failedLoginAttempts = user.getFailedLoginAttempts() != null ? user.getFailedLoginAttempts() : 0;
        user.setFailedLoginAttempts(failedLoginAttempts + 1);
        user.setLastFailedLoginDate(new Date());
        userService.save(user);
    }
}
