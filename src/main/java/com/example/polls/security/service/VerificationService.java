package com.example.polls.security.service;

import java.net.URI;

public interface VerificationService {

    void sendConfirmRegistrationMail(Long userId, URI confirmationUri);

    void verifyUserEmail(String token);

}
