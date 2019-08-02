package com.example.polls.service;

import com.example.polls.model.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findByUsername(String username);

    User save(User user);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    void recordSuccessAuthentication(User user);

    void recordFailedAuthenticationAttempt(User user);

    void lockUser(Long id);

    void unlockUser(Long id);
}
