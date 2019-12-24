package com.example.polls.security.listener;

import com.example.polls.security.event.RegistrationCompletedEvent;
import com.example.polls.security.service.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegistrationCompletedListener {

    private final VerificationService verificationService;

    @EventListener
    @Async
    public void onRegistrationComplete(RegistrationCompletedEvent event) {
        verificationService.sendConfirmRegistrationMail(event.getUserId(), event.getConfirmationUri());
    }
}
