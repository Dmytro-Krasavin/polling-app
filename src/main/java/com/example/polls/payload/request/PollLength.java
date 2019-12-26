package com.example.polls.payload.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Getter
@RequiredArgsConstructor
public class PollLength {

    @NotNull
    @Max(7)
    private final Integer days;

    @NotNull
    @Max(23)
    private final Integer hours;

}
