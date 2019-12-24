package com.example.polls.util.converter.request;

import com.example.polls.model.Role;
import com.example.polls.model.RoleType;
import com.example.polls.model.User;
import com.example.polls.payload.request.SignUpRequest;
import com.example.polls.service.RoleService;
import com.example.polls.util.converter.ModelConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class UserRequestConverter implements ModelConverter<SignUpRequest, User> {

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public User convert(SignUpRequest signUpRequest) {
        Assert.notNull(signUpRequest, "SignUpRequest must not be null!");

        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());
        Role userRole = roleService.findByType(RoleType.ROLE_USER);
        return new User(
                signUpRequest.getName(),
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encodedPassword,
                Collections.singleton(userRole)
        );
    }
}
