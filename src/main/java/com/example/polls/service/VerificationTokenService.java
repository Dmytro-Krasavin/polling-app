package com.example.polls.service;

import com.example.polls.model.User;

import java.util.Optional;

public interface VerificationTokenService<T> {

    Optional<T> findByToken(String token);

    T save(User user, String token);

}
