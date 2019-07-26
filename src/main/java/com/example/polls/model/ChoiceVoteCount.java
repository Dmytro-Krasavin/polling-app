package com.example.polls.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChoiceVoteCount {

    private Long choiceId;

    private Long voteCount;

}
