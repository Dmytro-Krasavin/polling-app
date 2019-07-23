package com.example.polls.controller;

import com.example.polls.converter.impl.AuthenticationToJwtTokenConverter;
import com.example.polls.converter.impl.RegisteredUserToResponseEntityConverter;
import com.example.polls.converter.impl.SignUpRequestToUserConverter;
import com.example.polls.model.User;
import com.example.polls.payload.ApiResponse;
import com.example.polls.payload.JwtAuthenticationResponse;
import com.example.polls.payload.LoginRequest;
import com.example.polls.payload.SignUpRequest;
import com.example.polls.security.UserLoginRequestAuthenticator;
import com.example.polls.security.handler.UserAuthenticationFailureHandler;
import com.example.polls.security.handler.UserAuthenticationSuccessHandler;
import com.example.polls.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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

    private final UserLoginRequestAuthenticator userLoginRequestAuthenticator;

    private final SignUpRequestToUserConverter userConverter;

    private final AuthenticationToJwtTokenConverter jwtTokenConverter;

    private final RegisteredUserToResponseEntityConverter responseFromUserConverter;

    private final UserAuthenticationSuccessHandler successHandler;

    private final UserAuthenticationFailureHandler failureHandler;

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = userLoginRequestAuthenticator.authenticate(loginRequest);
            JwtAuthenticationResponse jwtResponse = jwtTokenConverter.convert(authentication);
            successHandler.onAuthenticationSuccess(loginRequest);
            return ResponseEntity.ok(jwtResponse);
        } catch (BadCredentialsException e) {
            failureHandler.onAuthenticationFailure(loginRequest);
            throw e;
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        User user = userConverter.convert(signUpRequest);
        User savedUser = userService.save(user);
        return responseFromUserConverter.convert(savedUser);
    }
}
