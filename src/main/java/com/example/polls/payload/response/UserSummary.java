package com.example.polls.payload.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserSummary {

    private final Long id;

    private final String username;

    private final String name;

}
