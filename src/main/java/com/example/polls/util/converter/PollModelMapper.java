package com.example.polls.util.converter;

import com.example.polls.model.Choice;
import com.example.polls.model.Poll;
import com.example.polls.model.User;
import com.example.polls.payload.request.PollLength;
import com.example.polls.payload.request.PollRequest;
import com.example.polls.payload.response.ChoiceResponse;
import com.example.polls.payload.response.PollResponse;
import com.example.polls.payload.response.UserSummary;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PollModelMapper {

    public PollResponse mapPollToPollResponse(Poll poll, Map<Long, Long> choiceVotesMap, User creator, Long choiceId) {
        Instant now = Instant.now();
        boolean expired = poll.getExpirationDateTime().isBefore(now);

        List<Choice> choices = poll.getChoices();
        List<ChoiceResponse> choiceResponses = choices.stream()
                .map(choice -> mapChoiceToChoiceResponse(choice, choiceVotesMap))
                .collect(Collectors.toList());

        UserSummary creatorSummary = new UserSummary(creator.getId(), creator.getUsername(), creator.getName());

        long totalVotes = choiceResponses.stream()
                .mapToLong(ChoiceResponse::getVoteCount)
                .sum();

        return new PollResponse(
                poll.getId(),
                poll.getQuestion(),
                choiceResponses,
                creatorSummary,
                poll.getCreatedAt(),
                poll.getExpirationDateTime(),
                expired,
                choiceId,
                totalVotes
        );
    }

    public Poll mapPollRequestToPoll(PollRequest pollRequest) {
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

    private ChoiceResponse mapChoiceToChoiceResponse(Choice choice, Map<Long, Long> choiceVotesMap) {
        Long voteCount = choiceVotesMap.getOrDefault(choice.getId(), 0L);
        return new ChoiceResponse(choice.getId(), choice.getText(), voteCount);
    }
}
