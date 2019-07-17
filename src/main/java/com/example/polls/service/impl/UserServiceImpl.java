package com.example.polls.service.impl;

import com.example.polls.model.User;
import com.example.polls.repository.UserRepository;
import com.example.polls.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with id: " + id)
                );
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email: " + email)
                );
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public User findByUsernameOrEmail(String username, String email) {
        return userRepository.findByUsernameOrEmail(username, email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username: " + username + " or email : " + email)
                );
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<User> findByIdIn(List<Long> userIds) {
        return userRepository.findByIdIn(userIds);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username: " + username)
                );
    }

    @Override
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
