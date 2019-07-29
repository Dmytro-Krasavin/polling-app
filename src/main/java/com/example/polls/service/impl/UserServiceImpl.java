package com.example.polls.service.impl;

import com.example.polls.exception.model.UserNotFoundException;
import com.example.polls.model.User;
import com.example.polls.repository.UserRepository;
import com.example.polls.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User findById(Long id) throws UserNotFoundException {
        return fetchById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found with id: " + id)
                );
    }

    @Override
    public User findByEmail(String email) throws UserNotFoundException {
        return fetchByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found with email: " + email)
                );
    }

    @Override
    public User findByUsernameOrEmail(String username, String email) throws UserNotFoundException {
        return fetchByUsernameOrEmail(username, email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found with username: " + username + " or email : " + email)
                );
    }

    @Override
    public User findByUsername(String username) throws UserNotFoundException {
        return fetchByUsername(username)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found with username: " + username)
                );
    }

    @Override
    public List<User> findByIdIn(List<Long> userIds) {
        return userRepository.findByIdIn(userIds);
    }


    @Override
    public Optional<User> fetchById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public Optional<User> fetchByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> fetchByUsernameOrEmail(String username, String email) {
        return userRepository.findByUsernameOrEmail(username, email);
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public Optional<User> fetchByUsername(String username) {
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
}
