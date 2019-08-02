package com.example.polls.security.service;

public interface AuthenticationService<S, T> {

    T authenticate(S requestModel);

}
