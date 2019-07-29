package com.example.polls.security.handler;

import com.example.polls.model.User;
import com.example.polls.payload.request.LoginRequest;
import com.example.polls.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserAuthenticationFailureHandler {

    private final UserService userService;

    public void onAuthenticationFailure(LoginRequest loginRequest) {
        Assert.notNull(loginRequest, "LoginRequest must not be null!");

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
