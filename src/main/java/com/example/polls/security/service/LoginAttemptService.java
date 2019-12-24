package com.example.polls.security.service;

public interface LoginAttemptService {

    void loginSuccessful(String ipAddress);

    void loginFailed(String ipAddress);

    boolean isBlocked(String ipAddress);

}
