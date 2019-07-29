package com.example.polls.service.impl;

import com.example.polls.repository.VoteRepository;
import com.example.polls.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;

    @Override
    public long countByUserId(Long userId) {
        return voteRepository.countByUserId(userId);
    }
}
