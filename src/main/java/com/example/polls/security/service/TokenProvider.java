package com.example.polls.security.service;

import org.springframework.security.core.Authentication;

public interface TokenProvider {

    String generateToken(Authentication authentication);

    Long getUserIdFromToken(String token);

    boolean validateToken(String authToken);

}
