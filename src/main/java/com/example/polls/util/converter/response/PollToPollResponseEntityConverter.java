package com.example.polls.util.converter.response;

import com.example.polls.model.Poll;
import com.example.polls.payload.response.ApiResponse;
import com.example.polls.util.converter.ModelConverter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Component
public class PollToPollResponseEntityConverter implements ModelConverter<Poll, ResponseEntity<ApiResponse>> {

    @Override
    public ResponseEntity<ApiResponse> convert(Poll poll) {
        Assert.notNull(poll, "Poll must not be null!");

        Long pollId = poll.getId();
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{pollId}")
                .buildAndExpand(pollId)
                .toUri();
        ApiResponse apiResponse = new ApiResponse(true, "Poll Created Successfully");
        return ResponseEntity.created(location).body(apiResponse);
    }
}
