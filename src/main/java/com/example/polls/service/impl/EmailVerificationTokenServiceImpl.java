package com.example.polls.service.impl;

import com.example.polls.model.EmailVerificationToken;
import com.example.polls.model.User;
import com.example.polls.repository.EmailVerificationTokenRepository;
import com.example.polls.service.EmailVerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailVerificationTokenServiceImpl implements EmailVerificationTokenService {

    private final EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Override
    public Optional<EmailVerificationToken> findByToken(String token) {
        return emailVerificationTokenRepository.findByToken(token);
    }

    @Override
    public EmailVerificationToken save(User user, String token) {
        EmailVerificationToken verificationToken = new EmailVerificationToken(token, user);
        return emailVerificationTokenRepository.save(verificationToken);
    }
}
