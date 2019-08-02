package com.example.polls.controller;

import com.example.polls.model.User;
import com.example.polls.payload.request.LoginRequest;
import com.example.polls.payload.request.SignUpRequest;
import com.example.polls.payload.response.ApiResponse;
import com.example.polls.payload.response.JwtAuthenticationResponse;
import com.example.polls.security.event.AuthenticationFailedEvent;
import com.example.polls.security.event.AuthenticationSuccessfulEvent;
import com.example.polls.security.event.RegistrationCompletedEvent;
import com.example.polls.security.service.AuthenticationService;
import com.example.polls.security.service.VerificationService;
import com.example.polls.service.UserService;
import com.example.polls.util.converter.request.SignUpRequestToUserConverter;
import com.example.polls.util.converter.response.RegisteredUserToResponseEntityConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final VerificationService verificationService;
    private final AuthenticationService<LoginRequest, JwtAuthenticationResponse> authenticationService;
    private final SignUpRequestToUserConverter userConverter;
    private final RegisteredUserToResponseEntityConverter responseFromUserConverter;
    private final ApplicationEventPublisher eventPublisher;

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            JwtAuthenticationResponse jwtResponse = authenticationService.authenticate(loginRequest);
            eventPublisher.publishEvent(new AuthenticationSuccessfulEvent(loginRequest));
            return ResponseEntity.ok(jwtResponse);
        } catch (BadCredentialsException e) {
            eventPublisher.publishEvent(new AuthenticationFailedEvent(loginRequest));
            throw e;
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody SignUpRequest signUpRequest) {
        User user = userConverter.convert(signUpRequest);
        user = userService.save(user);

        URI confirmationUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/auth/verifyEmail").build().toUri();
        eventPublisher.publishEvent(new RegistrationCompletedEvent(user, confirmationUri));
        return responseFromUserConverter.convert(user);
    }

    @GetMapping("/verifyEmail")
    public void verifyEmail(@RequestParam("token") String token, HttpServletResponse response) throws IOException {
        verificationService.verifyUserEmail(token);
        String location = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
        response.sendRedirect(location);
    }
}
