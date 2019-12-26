package com.example.polls.payload.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class VoteRequest {

    @NotNull
    private final Long choiceId;

    @JsonCreator
    public VoteRequest(@NotNull Long choiceId) {
        this.choiceId = choiceId;
    }
}
