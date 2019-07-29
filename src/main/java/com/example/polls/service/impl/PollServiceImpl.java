package com.example.polls.service.impl;

import com.example.polls.exception.response.BadRequestException;
import com.example.polls.exception.response.ResourceNotFoundException;
import com.example.polls.model.*;
import com.example.polls.payload.request.PollRequest;
import com.example.polls.payload.request.VoteRequest;
import com.example.polls.payload.response.PagedResponse;
import com.example.polls.payload.response.PollResponse;
import com.example.polls.repository.PollRepository;
import com.example.polls.repository.UserRepository;
import com.example.polls.repository.VoteRepository;
import com.example.polls.security.UserPrincipal;
import com.example.polls.service.PollService;
import com.example.polls.util.AppConstants;
import com.example.polls.util.converter.request.PollRequestToPollConverter;
import com.example.polls.util.converter.response.PollToPollResponseConverter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PollServiceImpl implements PollService {

    private static final Logger logger = LoggerFactory.getLogger(PollServiceImpl.class);

    private final PollRepository pollRepository;
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final PollRequestToPollConverter pollRequestConverter;
    private final PollToPollResponseConverter pollResponseConverter;

    @Override
    public PagedResponse<PollResponse> getAllPolls(UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        // Retrieve Polls
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Poll> pollPage = pollRepository.findAll(pageable);
        if (pollPage.getNumberOfElements() == 0) {
            return createEmptyPagedResponse(pollPage);
        }

        // Map Polls to PollResponses containing vote counts and poll creator details
        List<Long> pollIds = pollPage.map(Poll::getId).getContent();
        Map<Long, Long> choiceVoteCountMap = getChoiceVoteCountMap(pollIds);
        Map<Long, Long> pollUserVoteMap = getPollUserVoteMap(currentUser, pollIds);
        Map<Long, User> creatorMap = getPollCreatorMap(pollPage.getContent());

        List<PollResponse> pollResponses = pollPage.map(poll -> {
                    Long choiceId = pollUserVoteMap == null ? null : pollUserVoteMap.get(poll.getId());
                    User creator = creatorMap.get(poll.getCreatedBy());
                    return pollResponseConverter.convert(poll, choiceVoteCountMap, creator, choiceId);
                }
        ).getContent();

        return createPagedResponse(pollPage, pollResponses);
    }

    @Override
    public PagedResponse<PollResponse> getPollsCreatedBy(String username, UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        // Retrieve all polls created by the given username
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Poll> pollPage = pollRepository.findByCreatedBy(user.getId(), pageable);
        if (pollPage.getNumberOfElements() == 0) {
            return createEmptyPagedResponse(pollPage);
        }

        // Map Polls to PollResponses containing vote counts and poll creator details
        List<Long> pollIds = pollPage.map(Poll::getId).getContent();
        Map<Long, Long> choiceVoteCountMap = getChoiceVoteCountMap(pollIds);
        Map<Long, Long> pollUserVoteMap = getPollUserVoteMap(currentUser, pollIds);

        List<PollResponse> pollResponses = pollPage.map(poll -> {
                    Long userVote = pollUserVoteMap == null ? null : pollUserVoteMap.get(poll.getId());
                    return pollResponseConverter.convert(poll, choiceVoteCountMap, user, userVote);
                }
        ).getContent();

        return createPagedResponse(pollPage, pollResponses);
    }

    @Override
    public PagedResponse<PollResponse> getPollsVotedBy(String username, UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        // Retrieve all pollIds in which the given username has voted
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Long> userVotedPollIds = voteRepository.findVotedPollIdsByUserId(user.getId(), pageable);
        if (userVotedPollIds.getNumberOfElements() == 0) {
            return createEmptyPagedResponse(userVotedPollIds);
        }

        // Retrieve all poll details from the voted pollIds.
        Sort sort = new Sort(Sort.Direction.DESC, "createdAt");
        List<Long> pollIds = userVotedPollIds.getContent();
        List<Poll> polls = pollRepository.findByIdIn(pollIds, sort);

        // Map Polls to PollResponses containing vote counts and poll creator details
        Map<Long, Long> choiceVoteCountMap = getChoiceVoteCountMap(pollIds);
        Map<Long, Long> pollUserVoteMap = getPollUserVoteMap(currentUser, pollIds);
        Map<Long, User> creatorMap = getPollCreatorMap(polls);

        List<PollResponse> pollResponses = polls.stream()
                .map(poll -> {
                            User creator = creatorMap.get(poll.getCreatedBy());
                            Long userVote = pollUserVoteMap == null ? null : pollUserVoteMap.get(poll.getId());
                            return pollResponseConverter.convert(poll, choiceVoteCountMap, creator, userVote);
                        }
                ).collect(Collectors.toList());

        return createPagedResponse(userVotedPollIds, pollResponses);
    }

    @Override
    public Poll createPoll(PollRequest pollRequest) {
        Poll poll = pollRequestConverter.convert(pollRequest);
        return pollRepository.save(poll);
    }

    @Override
    public PollResponse getPollById(Long pollId, UserPrincipal currentUser) {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new ResourceNotFoundException("Poll", "id", pollId));

        // Retrieve Vote Counts of every choice belonging to the current poll
        List<ChoiceVoteCount> votes = voteRepository.countByPollIdGroupByChoiceId(pollId);
        Map<Long, Long> choiceVotesMap = votes.stream()
                .collect(Collectors.toMap(
                        ChoiceVoteCount::getChoiceId,
                        ChoiceVoteCount::getVoteCount)
                );

        // Retrieve poll creator details
        User creator = userRepository.findById(poll.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", poll.getCreatedBy()));

        // Retrieve vote done by logged in user
        Vote userVote = null;
        if (currentUser != null) {
            userVote = voteRepository.findByUserIdAndPollId(currentUser.getId(), pollId);
        }

        Long choiceId = userVote != null ? userVote.getChoice().getId() : null;
        return pollResponseConverter.convert(poll, choiceVotesMap, creator, choiceId);
    }

    @Override
    public PollResponse castVoteAndGetUpdatedPoll(Long pollId, VoteRequest voteRequest, UserPrincipal currentUser) {
        User user = userRepository.getOne(currentUser.getId());
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new ResourceNotFoundException("Poll", "id", pollId));
        if (poll.getExpirationDateTime().isBefore(Instant.now())) {
            throw new BadRequestException("Sorry! This Poll has already expired");
        }

        List<Choice> choices = poll.getChoices();
        Choice selectedChoice = choices.stream()
                .filter(choice -> choice.getId().equals(voteRequest.getChoiceId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Choice", "id", voteRequest.getChoiceId()));

        Vote vote = new Vote();
        vote.setPoll(poll);
        vote.setUser(user);
        vote.setChoice(selectedChoice);
        try {
            vote = voteRepository.save(vote);
        } catch (DataIntegrityViolationException ex) {
            logger.info("User {} has already voted in Poll {}", currentUser.getId(), pollId);
            throw new BadRequestException("Sorry! You have already cast your vote in this poll");
        }

        //-- Vote Saved, Return the updated Poll Response now --

        // Retrieve Vote Counts of every choice belonging to the current poll
        List<ChoiceVoteCount> votes = voteRepository.countByPollIdGroupByChoiceId(pollId);
        Map<Long, Long> choiceVotesMap = votes.stream()
                .collect(Collectors.toMap(
                        ChoiceVoteCount::getChoiceId,
                        ChoiceVoteCount::getVoteCount)
                );

        // Retrieve poll creator details
        Long creatorId = poll.getCreatedBy();
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", creatorId));

        return pollResponseConverter.convert(poll, choiceVotesMap, creator, vote.getChoice().getId());
    }

    @Override
    public long countByCreatedBy(Long userId) {
        return pollRepository.countByCreatedBy(userId);
    }

    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

    private Map<Long, Long> getChoiceVoteCountMap(List<Long> pollIds) {
        // Retrieve Vote Counts of every Choice belonging to the given pollIds
        List<ChoiceVoteCount> votes = voteRepository.countByPollIdInGroupByChoiceId(pollIds);
        return votes.stream()
                .collect(Collectors.toMap(
                        ChoiceVoteCount::getChoiceId,
                        ChoiceVoteCount::getVoteCount)
                );
    }

    private Map<Long, Long> getPollUserVoteMap(UserPrincipal currentUser, List<Long> pollIds) {
        // Retrieve Votes done by the logged in user to the given pollIds
        if (currentUser == null) {
            return new HashMap<>();
        }

        List<Vote> userVotes = voteRepository.findByUserIdAndPollIdIn(currentUser.getId(), pollIds);
        return userVotes.stream()
                .collect(Collectors.toMap(
                        vote -> vote.getPoll().getId(),
                        vote -> vote.getChoice().getId()
                ));
    }

    private Map<Long, User> getPollCreatorMap(List<Poll> polls) {
        // Get Poll Creator details of the given list of polls
        List<Long> creatorIds = polls.stream()
                .map(Poll::getCreatedBy)
                .distinct()
                .collect(Collectors.toList());

        List<User> creators = userRepository.findByIdIn(creatorIds);
        return creators.stream()
                .collect(Collectors.toMap(
                        User::getId,
                        Function.identity()
                ));
    }

    private <T> PagedResponse<T> createPagedResponse(Page page, List<T> content) {
        return new PagedResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    private <T> PagedResponse<T> createEmptyPagedResponse(Page page) {
        return createPagedResponse(page, Collections.emptyList());
    }
}
