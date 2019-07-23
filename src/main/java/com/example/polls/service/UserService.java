package com.example.polls.service;

import com.example.polls.exception.model.UserNotFoundException;
import com.example.polls.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User findById(Long id) throws UserNotFoundException;

    User findByEmail(String email) throws UserNotFoundException;

    User findByUsernameOrEmail(String username, String email) throws UserNotFoundException;

    List<User> findByIdIn(List<Long> userIds);

    User findByUsername(String username) throws UserNotFoundException;

    Optional<User> fetchById(Long id);

    Optional<User> fetchByEmail(String email);

    Optional<User> fetchByUsernameOrEmail(String username, String email);

    Optional<User> fetchByUsername(String username);

    User save(User user);

}
