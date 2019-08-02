package com.example.polls.security.service;

import com.example.polls.model.User;

import java.net.URI;

public interface VerificationService {

    void sendConfirmRegistrationMail(User user, URI confirmationUri);

    void verifyUserEmail(String token);

}
