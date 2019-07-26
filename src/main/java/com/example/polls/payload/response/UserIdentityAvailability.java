package com.example.polls.payload.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserIdentityAvailability {

    private final boolean available;

}
