package com.example.polls.validation.impl;

import com.example.polls.exception.model.validation.impl.SignUpRequestValidationException;
import com.example.polls.payload.SignUpRequest;
import com.example.polls.service.UserService;
import com.example.polls.validation.ModelValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SignUpRequestValidator implements ModelValidator<SignUpRequest> {

    private final UserService userService;

    public void validate(SignUpRequest signUpRequest) throws SignUpRequestValidationException {
        if (userService.existsByUsername(signUpRequest.getUsername())) {
            throw new SignUpRequestValidationException("Username is already taken!");
        }

        if (userService.existsByEmail(signUpRequest.getEmail())) {
            throw new SignUpRequestValidationException("Email Address already in use!");
        }
    }
}
