package com.example.polls.payload.request;

import com.example.polls.model.Choice;
import com.example.polls.model.Poll;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class PollRequest {

    @NotBlank
    @Size(max = 140)
    private final String question;

    @NotNull
    @Size(min = 2, max = 6)
    @Valid
    private final List<ChoiceRequest> choices;

    @NotNull
    @Valid
    private final PollLength pollLength;

    public Poll toPoll() {
        Instant expirationDateTime = Instant.now()
                .plus(Duration.ofDays(pollLength.getDays()))
                .plus(Duration.ofHours(pollLength.getHours()));
        Poll poll = new Poll(question, expirationDateTime);
        choices.forEach(choiceRequest -> poll.addChoice(new Choice(choiceRequest.getText())));
        return poll;
    }
}
