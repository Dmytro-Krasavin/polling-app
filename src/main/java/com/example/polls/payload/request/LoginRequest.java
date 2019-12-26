package com.example.polls.payload.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@RequiredArgsConstructor
public class LoginRequest {

    @NotBlank
    private final String usernameOrEmail;

    @NotBlank
    private final String password;

}
