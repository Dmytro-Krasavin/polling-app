package com.example.polls.util.converter.request;

import com.example.polls.model.Choice;
import com.example.polls.model.Poll;
import com.example.polls.payload.request.PollLength;
import com.example.polls.payload.request.PollRequest;
import com.example.polls.util.converter.ModelConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.Duration;
import java.time.Instant;

@Component
public class PollRequestToPollConverter implements ModelConverter<PollRequest, Poll> {

    @Override
    public Poll convert(PollRequest pollRequest) {
        Assert.notNull(pollRequest, "PollRequest must not be null!");

        Poll poll = new Poll();
        poll.setQuestion(pollRequest.getQuestion());

        pollRequest.getChoices().forEach(choiceRequest -> poll.addChoice(new Choice(choiceRequest.getText())));

        Instant now = Instant.now();
        PollLength pollLength = pollRequest.getPollLength();
        Instant expirationDateTime = now
                .plus(Duration.ofDays(pollLength.getDays()))
                .plus(Duration.ofHours(pollLength.getHours()));
        poll.setExpirationDateTime(expirationDateTime);

        return poll;
    }
}
