package com.example.polls.controller;

import com.example.polls.converter.impl.JwtTokenFromAuthenticationConverter;
import com.example.polls.converter.impl.ResponseEntityFromRegisteredUserConverter;
import com.example.polls.converter.impl.ResponseEntityFromValidationExceptionConverter;
import com.example.polls.converter.impl.UserFromSignUpRequestConverter;
import com.example.polls.exception.model.SignUpRequestValidationException;
import com.example.polls.model.User;
import com.example.polls.payload.JwtAuthenticationResponse;
import com.example.polls.payload.LoginRequest;
import com.example.polls.payload.SignUpRequest;
import com.example.polls.security.UserLoginRequestAuthenticator;
import com.example.polls.service.UserService;
import com.example.polls.validator.impl.SignUpRequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    private final UserFromSignUpRequestConverter userConverter;

    private final UserLoginRequestAuthenticator userLoginRequestAuthenticator;

    private final JwtTokenFromAuthenticationConverter jwtTokenConverter;

    private final ResponseEntityFromRegisteredUserConverter responseFromUserConverter;

    private final ResponseEntityFromValidationExceptionConverter responseFromValidationExceptionConverter;

    private final SignUpRequestValidator signUpRequestValidator;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = userLoginRequestAuthenticator.authenticate(loginRequest);
        JwtAuthenticationResponse jwtResponse = jwtTokenConverter.convert(authentication);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        try {
            signUpRequestValidator.validate(signUpRequest);
        } catch (SignUpRequestValidationException e) {
            return responseFromValidationExceptionConverter.convert(e);
        }

        User user = userConverter.convert(signUpRequest);
        User savedUser = userService.save(user);
        return responseFromUserConverter.convert(savedUser);
    }
}
