package com.example.polls.validator.impl;

import com.example.polls.exception.model.SignUpRequestValidationException;
import com.example.polls.payload.SignUpRequest;
import com.example.polls.service.UserService;
import com.example.polls.validator.ModelValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SignUpRequestValidator implements ModelValidator<SignUpRequest> {

    private final UserService userService;

    @Autowired
    public SignUpRequestValidator(UserService userService) {
        this.userService = userService;
    }

    public void validate(SignUpRequest signUpRequest) throws SignUpRequestValidationException {
        if (userService.existsByUsername(signUpRequest.getUsername())) {
            throw new SignUpRequestValidationException("Username is already taken!");
        }

        if (userService.existsByEmail(signUpRequest.getEmail())) {
            throw new SignUpRequestValidationException("Email Address already in use!");
        }
    }
}
