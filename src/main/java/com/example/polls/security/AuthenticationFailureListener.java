package com.example.polls.security;

import com.example.polls.model.User;
import com.example.polls.payload.request.LoginRequest;
import com.example.polls.security.event.OnAuthenticationFailureEvent;
import com.example.polls.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthenticationFailureListener implements ApplicationListener<OnAuthenticationFailureEvent> {

    private final UserService userService;

    @Override
    public void onApplicationEvent(OnAuthenticationFailureEvent event) {
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
