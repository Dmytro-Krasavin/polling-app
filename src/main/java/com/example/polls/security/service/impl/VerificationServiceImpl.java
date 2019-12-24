package com.example.polls.security.service.impl;

import com.example.polls.exception.BadRequestException;
import com.example.polls.exception.ResourceNotFoundException;
import com.example.polls.model.EmailVerificationToken;
import com.example.polls.model.User;
import com.example.polls.security.service.VerificationService;
import com.example.polls.service.EmailVerificationTokenService;
import com.example.polls.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {

    private final UserService userService;
    private final EmailVerificationTokenService emailVerificationTokenService;
    private final JavaMailSender mailSender;

    @Override
    public void sendConfirmRegistrationMail(Long userId, URI confirmationUri) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        String token = UUID.randomUUID().toString();
        emailVerificationTokenService.save(user, token);

        String confirmationUriString = ServletUriComponentsBuilder.fromUri(confirmationUri)
                .queryParam("token", token).toUriString();
        SimpleMailMessage mailMessage = buildConfirmRegistrationMailMessage(user.getEmail(), confirmationUriString);
        mailSender.send(mailMessage);
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

    private SimpleMailMessage buildConfirmRegistrationMailMessage(String emailAddress, String confirmationUri) {
        String subject = "Registration Confirmation";
        String messageText = "Click on the link to confirm your registration: ";

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(emailAddress);
        mailMessage.setSubject(subject);
        mailMessage.setText(messageText + confirmationUri);
        return mailMessage;
    }
}
