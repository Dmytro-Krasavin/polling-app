package com.example.polls.service;

import com.example.polls.model.EmailVerificationToken;
import com.example.polls.model.User;

import java.util.Optional;

public interface EmailVerificationTokenService {

    Optional<EmailVerificationToken> findByToken(String token);

    EmailVerificationToken save(User user, String token);
}
