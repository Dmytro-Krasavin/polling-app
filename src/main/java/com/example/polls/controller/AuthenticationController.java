package com.example.polls.controller;

import com.example.polls.payload.request.LoginRequest;
import com.example.polls.payload.request.SignUpRequest;
import com.example.polls.payload.response.JwtAuthenticationResponse;
import com.example.polls.payload.response.UserResponse;
import com.example.polls.security.service.AuthenticationService;
import com.example.polls.security.service.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final VerificationService verificationService;

    @PostMapping("/signin")
    public JwtAuthenticationResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authenticationService.authenticateUser(loginRequest);
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse registerUser(@RequestBody SignUpRequest signUpRequest) {
        return authenticationService.registerUser(signUpRequest);
    }

    @GetMapping("/verifyEmail")
    public void verifyEmail(@RequestParam("token") String token, HttpServletResponse response) throws IOException {
        verificationService.verifyUserEmail(token);
        String location = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
        response.sendRedirect(location);
    }
}
