package com.example.polls.controller;

import com.example.polls.model.User;
import com.example.polls.payload.request.LoginRequest;
import com.example.polls.payload.request.SignUpRequest;
import com.example.polls.payload.response.ApiResponse;
import com.example.polls.payload.response.JwtAuthenticationResponse;
import com.example.polls.security.event.OnAuthenticationFailureEvent;
import com.example.polls.security.event.OnAuthenticationSuccessEvent;
import com.example.polls.security.service.TokenProvider;
import com.example.polls.security.service.UserAuthenticator;
import com.example.polls.service.UserService;
import com.example.polls.util.converter.request.SignUpRequestToUserConverter;
import com.example.polls.util.converter.response.RegisteredUserToResponseEntityConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher eventPublisher;

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = userAuthenticator.authenticate(loginRequest);
            String jwt = tokenProvider.generateToken(authentication);
            JwtAuthenticationResponse jwtResponse = new JwtAuthenticationResponse(jwt);
            eventPublisher.publishEvent(new OnAuthenticationSuccessEvent(loginRequest));
            return ResponseEntity.ok(jwtResponse);
        } catch (BadCredentialsException e) {
            eventPublisher.publishEvent(new OnAuthenticationFailureEvent(loginRequest));
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
