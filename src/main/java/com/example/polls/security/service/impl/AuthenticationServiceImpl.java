package com.example.polls.security.service.impl;

import com.example.polls.model.User;
import com.example.polls.payload.request.LoginRequest;
import com.example.polls.payload.request.SignUpRequest;
import com.example.polls.payload.response.JwtAuthenticationResponse;
import com.example.polls.payload.response.UserResponse;
import com.example.polls.security.service.AuthenticationService;
import com.example.polls.security.service.TokenProvider;
import com.example.polls.service.UserService;
import com.example.polls.util.converter.ModelConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final UserService userService;

    private final ModelConverter<SignUpRequest, User> userRequestConverter;
    private final ModelConverter<User, UserResponse> userResponseConverter;

    @Override
    public JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsernameOrEmail(),
                loginRequest.getPassword()
        );
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return new JwtAuthenticationResponse(jwt);
    }

    @Override
    public UserResponse registerUser(SignUpRequest signUpRequest) {
        User user = userRequestConverter.convert(signUpRequest);
        user = userService.save(user);
        return userResponseConverter.convert(user);
    }
}
