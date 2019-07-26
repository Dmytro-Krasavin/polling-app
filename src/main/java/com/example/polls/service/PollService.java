package com.example.polls.service;

import com.example.polls.model.Poll;
import com.example.polls.payload.request.PollRequest;
import com.example.polls.payload.request.VoteRequest;
import com.example.polls.payload.response.PagedResponse;
import com.example.polls.payload.response.PollResponse;
import com.example.polls.security.UserPrincipal;

public interface PollService {

    PagedResponse<PollResponse> getAllPolls(UserPrincipal currentUser, int page, int size);

    PagedResponse<PollResponse> getPollsCreatedBy(String username, UserPrincipal currentUser, int page, int size);

    PagedResponse<PollResponse> getPollsVotedBy(String username, UserPrincipal currentUser, int page, int size);

    Poll createPoll(PollRequest pollRequest);

    PollResponse getPollById(Long pollId, UserPrincipal currentUser);

    PollResponse castVoteAndGetUpdatedPoll(Long pollId, VoteRequest voteRequest, UserPrincipal currentUser);

}
