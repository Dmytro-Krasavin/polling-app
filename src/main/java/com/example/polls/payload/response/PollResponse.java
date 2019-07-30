package com.example.polls.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@Getter
@RequiredArgsConstructor
public class PollResponse {

    private final Long id;

    private final String question;

    private final List<ChoiceResponse> choices;

    private final UserSummary createdBy;

    private final Instant creationDateTime;

    private final Instant expirationDateTime;

    private final boolean expired;

    @JsonInclude(Include.NON_NULL)
    private final Long selectedChoice;

    private final Long totalVotes;

}
