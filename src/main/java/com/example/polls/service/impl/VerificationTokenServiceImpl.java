package com.example.polls.service.impl;

import com.example.polls.model.User;
import com.example.polls.model.VerificationToken;
import com.example.polls.repository.VerificationTokenRepository;
import com.example.polls.service.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;

    @Override
    public Optional<VerificationToken> findByToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    @Override
    public VerificationToken save(User user, String token) {
        VerificationToken verificationToken = new VerificationToken(token, user);
        return verificationTokenRepository.save(verificationToken);
    }
}
