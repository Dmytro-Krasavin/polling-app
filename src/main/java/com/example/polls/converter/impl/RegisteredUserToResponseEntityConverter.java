package com.example.polls.converter.impl;

import com.example.polls.converter.ModelConverter;
import com.example.polls.model.User;
import com.example.polls.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class RegisteredUserToResponseEntityConverter implements ModelConverter<User, ResponseEntity> {

    @Override
    public ResponseEntity<ApiResponse> convert(User user) {
        Assert.notNull(user, "User must not be null!");
        String username = user.getUsername();
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(username).toUri();

        ApiResponse responseBody = new ApiResponse(true, "User registered successfully");
        return ResponseEntity.created(location).body(responseBody);
    }
}
