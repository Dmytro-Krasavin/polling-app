package com.example.polls.security.listener;

import com.example.polls.model.User;
import com.example.polls.security.model.UserPrincipal;
import com.example.polls.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuditingAuthenticationListener {

    private final UserService userService;

    @EventListener
    @Async
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        Authentication authentication = event.getAuthentication();
        Optional<User> user = extractUserFromAuthentication(authentication);
        user.ifPresent(userService::recordSuccessAuthentication);
    }

    @EventListener
    @Async
    public void onAuthenticationFailBadCredentials(AuthenticationFailureBadCredentialsEvent event) {
        Authentication authentication = event.getAuthentication();
        Optional<User> user = extractUserFromAuthentication(authentication);
        user.ifPresent(userService::recordFailedAuthenticationAttempt);
    }

    private Optional<User> extractUserFromAuthentication(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserPrincipal) {
            Long userId = ((UserPrincipal) principal).getId();
            return userService.findById(userId);
        }
        String usernameOrEmail = authentication.getName();
        return userService.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
    }
}
