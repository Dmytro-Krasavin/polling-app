package com.example.polls.util.converter.impl;

import com.example.polls.util.converter.ModelConverter;
import com.example.polls.model.Role;
import com.example.polls.model.RoleType;
import com.example.polls.model.User;
import com.example.polls.payload.request.SignUpRequest;
import com.example.polls.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class SignUpRequestToUserConverter implements ModelConverter<SignUpRequest, User> {

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public User convert(SignUpRequest signUpRequest) {
        Assert.notNull(signUpRequest, "SignUpRequest must not be null!");

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