package com.example.polls.service.impl;

import com.example.polls.exception.ResourceNotFoundException;
import com.example.polls.model.User;
import com.example.polls.repository.UserRepository;
import com.example.polls.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByUsernameOrEmail(String username, String email) {
        return userRepository.findByUsernameOrEmail(username, email);
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    @Override
    public User save(User user) {
        return userRepository.save(user);
    }


    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }


    @Override
    @Transactional
    public void recordSuccessAuthentication(User user) {
        user.setLastLoginDate(new Date());
        save(user);
    }

    @Override
    @Transactional
    public void recordFailedAuthenticationAttempt(User user) {
        int failedLoginAttempts = user.getFailedLoginAttempts() != null ? user.getFailedLoginAttempts() : 0;
        user.setFailedLoginAttempts(failedLoginAttempts + 1);
        user.setLastFailedLoginDate(new Date());
        save(user);
    }

    @Override
    @Transactional
    public void lockUser(Long id) {
        changeUserLockStatus(id, true);
    }

    @Override
    @Transactional
    public void unlockUser(Long id) {
        changeUserLockStatus(id, false);
    }

    private void changeUserLockStatus(Long id, boolean locked) {
        User user = findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        user.setLocked(locked);
        save(user);
    }
}
