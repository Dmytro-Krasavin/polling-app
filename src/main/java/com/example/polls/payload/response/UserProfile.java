package com.example.polls.payload.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@Getter
@RequiredArgsConstructor
public class UserProfile {

    private final Long id;

    private final String username;

    private final String name;

    private final Instant joinedAt;

    private final Long pollCount;

    private final Long voteCount;

}
