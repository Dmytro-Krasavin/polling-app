package com.example.polls.security.service.impl;

import com.example.polls.exception.BadRequestException;
import com.example.polls.model.EmailVerificationToken;
import com.example.polls.model.User;
import com.example.polls.security.service.VerificationService;
import com.example.polls.service.EmailVerificationTokenService;
import com.example.polls.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {

    private final UserService userService;
    private final EmailVerificationTokenService emailVerificationTokenService;
    private final JavaMailSender mailSender;

    @Override
    public void sendConfirmRegistrationMail(User user, URI confirmationUri) {
        String token = UUID.randomUUID().toString();
        emailVerificationTokenService.save(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String message = "Click on the link to confirm your registration: ";
        String confirmationUriString = ServletUriComponentsBuilder.fromUri(confirmationUri)
                .queryParam("token", token).toUriString();

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + confirmationUriString);
        mailSender.send(email);
    }

    @Override
    public void verifyUserEmail(String token) {
        EmailVerificationToken verificationToken = emailVerificationTokenService.findByToken(token)
                .orElseThrow(() -> new BadRequestException("Invalid token"));
        if (verificationToken.isExpired()) {
            throw new BadRequestException("Token is expired");
        }

        User user = verificationToken.getUser();
        user.setEmailVerified(true);
        userService.save(user);
    }
}
