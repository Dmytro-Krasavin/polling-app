package com.example.polls.controller;

import com.example.polls.model.User;
import com.example.polls.payload.request.LoginRequest;
import com.example.polls.payload.request.SignUpRequest;
import com.example.polls.payload.response.ApiResponse;
import com.example.polls.payload.response.JwtAuthenticationResponse;
import com.example.polls.security.handler.UserAuthenticationFailureHandler;
import com.example.polls.security.handler.UserAuthenticationSuccessHandler;
import com.example.polls.security.service.TokenProvider;
import com.example.polls.security.service.UserAuthenticator;
import com.example.polls.service.UserService;
import com.example.polls.util.converter.response.RegisteredUserToResponseEntityConverter;
import com.example.polls.util.converter.request.SignUpRequestToUserConverter;
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

    private final UserAuthenticator<LoginRequest> userAuthenticator;

    private final SignUpRequestToUserConverter userConverter;

    private final TokenProvider tokenProvider;

    private final RegisteredUserToResponseEntityConverter responseFromUserConverter;

    private final UserAuthenticationSuccessHandler successHandler;

    private final UserAuthenticationFailureHandler failureHandler;

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = userAuthenticator.authenticate(loginRequest);
            String jwt = tokenProvider.generateToken(authentication);
            JwtAuthenticationResponse jwtResponse = new JwtAuthenticationResponse(jwt);
            successHandler.onAuthenticationSuccess(loginRequest);
            return ResponseEntity.ok(jwtResponse);
        } catch (BadCredentialsException e) {
            failureHandler.onAuthenticationFailure(loginRequest);
            throw e;
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody SignUpRequest signUpRequest) {
        User user = userConverter.convert(signUpRequest);
        User savedUser = userService.save(user);
        return responseFromUserConverter.convert(savedUser);
    }
}
