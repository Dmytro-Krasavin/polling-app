package com.example.polls.security.service;

import org.springframework.security.core.Authentication;

public interface UserAuthenticator<T> {

    Authentication authenticate(T model);

}
