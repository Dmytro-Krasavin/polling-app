package com.example.polls.controller;

import com.example.polls.converter.impl.JwtAuthenticationResponseFromLoginRequestConverter;
import com.example.polls.converter.impl.UserFromSignUpRequestConverter;
import com.example.polls.exception.model.SignUpRequestValidationException;
import com.example.polls.model.User;
import com.example.polls.payload.ApiResponse;
import com.example.polls.payload.JwtAuthenticationResponse;
import com.example.polls.payload.LoginRequest;
import com.example.polls.payload.SignUpRequest;
import com.example.polls.service.UserService;
import com.example.polls.validator.impl.SignUpRequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    private final UserFromSignUpRequestConverter userConverter;

    private final JwtAuthenticationResponseFromLoginRequestConverter jwtResponseConverter;

    private final SignUpRequestValidator signUpRequestValidator;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtAuthenticationResponse jwtResponse = jwtResponseConverter.convert(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        try {
            signUpRequestValidator.validate(signUpRequest);
        } catch (SignUpRequestValidationException e) {
            ApiResponse responseBody = new ApiResponse(false, e.getMessage());
            return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
        }

        User user = userConverter.convert(signUpRequest);
        User savedUser = userService.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(savedUser.getUsername()).toUri();

        ApiResponse responseBody = new ApiResponse(true, "User registered successfully");
        return ResponseEntity.created(location).body(responseBody);
    }
}
