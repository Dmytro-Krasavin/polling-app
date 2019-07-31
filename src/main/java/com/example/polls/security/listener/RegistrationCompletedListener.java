package com.example.polls.security.listener;

import com.example.polls.model.User;
import com.example.polls.security.event.RegistrationCompletedEvent;
import com.example.polls.service.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RegistrationCompletedListener {

    private final VerificationTokenService tokenService;
    private final JavaMailSender mailSender;

    @EventListener
    @Async
    public void onRegistrationComplete(RegistrationCompletedEvent event) {
        sendConfirmRegistrationMail(event.getUser(), event.getConfirmationUri());
    }

    private void sendConfirmRegistrationMail(User user, URI confirmationUri) {
        String token = UUID.randomUUID().toString();
        tokenService.save(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUriString = ServletUriComponentsBuilder.fromUri(confirmationUri)
                .queryParam("token", token).toUriString();
        String message = "Click on the link to confirm your registration: ";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + " " + confirmationUriString);
        mailSender.send(email);
    }
}
