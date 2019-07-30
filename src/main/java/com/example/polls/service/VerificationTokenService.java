package com.example.polls.service;

import com.example.polls.model.User;
import com.example.polls.model.VerificationToken;

import java.util.Optional;

public interface VerificationTokenService {

    Optional<VerificationToken> findByToken(String token);

    VerificationToken save(User user, String token);
}
