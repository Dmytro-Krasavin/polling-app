package com.example.polls.payload.response;

import lombok.Getter;

@Getter
public class JwtAuthenticationResponse {

    private final String accessToken;

    private final String tokenType = "Bearer";

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
