package com.example.polls.util.converter.response;

import com.example.polls.model.User;
import com.example.polls.payload.response.UserResponse;
import com.example.polls.util.converter.ModelConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class UserResponseConverter implements ModelConverter<User, UserResponse> {

    @Override
    public UserResponse convert(User user) {
        Assert.notNull(user, "User must not be null!");

        String username = user.getUsername();
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/users/{username}")
                .buildAndExpand(username)
                .toUri();
        return new UserResponse(user.getId(), user.getUsername(), user.getName(), location);
    }
}
