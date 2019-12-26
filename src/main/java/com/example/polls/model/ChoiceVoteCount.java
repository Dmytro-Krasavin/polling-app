package com.example.polls.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChoiceVoteCount {

    private final Long choiceId;

    private final Long voteCount;

}
