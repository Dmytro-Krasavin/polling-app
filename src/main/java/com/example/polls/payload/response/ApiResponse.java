package com.example.polls.payload.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiResponse {

    private final Boolean success;

    private final String message;

}
