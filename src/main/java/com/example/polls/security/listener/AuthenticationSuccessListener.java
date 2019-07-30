package com.example.polls.security.listener;

import com.example.polls.model.User;
import com.example.polls.payload.request.LoginRequest;
import com.example.polls.security.event.OnAuthenticationSuccessEvent;
import com.example.polls.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthenticationSuccessListener implements ApplicationListener<OnAuthenticationSuccessEvent> {

    private final UserService userService;

    @Override
    public void onApplicationEvent(OnAuthenticationSuccessEvent event) {
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
