package com.example.polls.security.listener;

import com.example.polls.security.service.LoginAttemptService;
import com.example.polls.util.IpAddressExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginAttemptAuthenticationListener {

    private final LoginAttemptService loginAttemptService;

    private final IpAddressExtractor ipAddressExtractor;

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        String ip = ipAddressExtractor.getClientIP();
        loginAttemptService.loginSuccessful(ip);
    }

    @EventListener
    public void onAuthenticationFailBadCredentials(AuthenticationFailureBadCredentialsEvent event) {
        String ip = ipAddressExtractor.getClientIP();
        loginAttemptService.loginFailed(ip);
    }
}
