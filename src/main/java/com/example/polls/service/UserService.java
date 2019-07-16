package com.example.polls.service;

import com.example.polls.model.User;

import java.util.List;

public interface UserService {

    User findById(Long id);

    User findByEmail(String email);

    User findByUsernameOrEmail(String username, String email);

    List<User> findByIdIn(List<Long> userIds);

    User findByUsername(String username);

    User save(User user);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

}
