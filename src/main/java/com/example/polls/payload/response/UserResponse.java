package com.example.polls.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.net.URI;

@Getter
@RequiredArgsConstructor
public class UserResponse {

    private final Long id;

    private final String username;

    private final String name;

    @JsonIgnore
    private final URI location;

}
