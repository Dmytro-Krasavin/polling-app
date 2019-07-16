package com.example.polls.converter.impl;

import com.example.polls.converter.ModelConverter;
import com.example.polls.model.Role;
import com.example.polls.model.RoleType;
import com.example.polls.model.User;
import com.example.polls.payload.SignUpRequest;
import com.example.polls.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class UserFromSignUpRequestConverter implements ModelConverter<SignUpRequest, User> {

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserFromSignUpRequestConverter(RoleService roleService, PasswordEncoder passwordEncoder) {
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User convert(SignUpRequest signUpRequest) {
        User user = new User(
                signUpRequest.getName(),
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                signUpRequest.getPassword()
        );
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleService.findByType(RoleType.USER);
        user.setRoles(Collections.singleton(userRole));
        return user;
    }
}
