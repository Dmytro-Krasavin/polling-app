package com.example.polls.controller;

import com.example.polls.exception.BadRequestException;
import com.example.polls.model.User;
import com.example.polls.model.VerificationToken;
import com.example.polls.payload.request.LoginRequest;
import com.example.polls.payload.request.SignUpRequest;
import com.example.polls.payload.response.ApiResponse;
import com.example.polls.payload.response.JwtAuthenticationResponse;
import com.example.polls.security.event.OnAuthenticationFailureEvent;
import com.example.polls.security.event.OnAuthenticationSuccessEvent;
import com.example.polls.security.event.OnRegistrationCompleteEvent;
import com.example.polls.security.service.TokenProvider;
import com.example.polls.security.service.UserAuthenticator;
import com.example.polls.service.UserService;
import com.example.polls.service.VerificationTokenService;
import com.example.polls.util.converter.request.SignUpRequestToUserConverter;
import com.example.polls.util.converter.response.RegisteredUserToResponseEntityConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.Instant;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final VerificationTokenService tokenService;
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
        user = userService.save(user);

        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/auth/verifyEmail").build().toUri();
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, uri));
        return responseFromUserConverter.convert(user);
    }

    @GetMapping("/verifyEmail")
    public ResponseEntity<ApiResponse> verifyEmail(@RequestParam("token") String token) {
        VerificationToken verificationToken = tokenService.findByToken(token)
                .orElseThrow(() -> new BadRequestException("Invalid token"));

        Instant expirationDate = verificationToken.getExpirationDate();
        if (Instant.now().isAfter(expirationDate)) {
            throw new BadRequestException("Token is expired");
        }

        User user = verificationToken.getUser();
        user.setEmailVerified(true);
        user = userService.save(user);
        return responseFromUserConverter.convert(user);
    }
}
