package com.example.polls.payload.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChoiceResponse {

    private final long id;

    private final String text;

    private final long voteCount;

}
