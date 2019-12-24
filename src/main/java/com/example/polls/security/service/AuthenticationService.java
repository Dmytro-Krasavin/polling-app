package com.example.polls.security.service;

import com.example.polls.payload.request.LoginRequest;
import com.example.polls.payload.request.SignUpRequest;
import com.example.polls.payload.response.JwtAuthenticationResponse;
import com.example.polls.payload.response.UserResponse;

public interface AuthenticationService {

    JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest);

    UserResponse registerUser(SignUpRequest signUpRequest);

}
