package com.example.polls.security.handler;

import com.example.polls.exception.model.UserNotFoundException;
import com.example.polls.model.User;
import com.example.polls.payload.request.LoginRequest;
import com.example.polls.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class UserAuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(UserAuthenticationSuccessHandler.class);

    private final UserService userService;

    public void onAuthenticationSuccess(LoginRequest loginRequest) {
        Assert.notNull(loginRequest, "LoginRequest must not be null!");

        String usernameOrEmail = loginRequest.getUsernameOrEmail();
        try {
            User user = userService.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
            user.setLastLoginDate(new Date());
            userService.save(user);
        } catch (UserNotFoundException e) {
            logger.warn("Could not find user", e);
        }
    }
}
