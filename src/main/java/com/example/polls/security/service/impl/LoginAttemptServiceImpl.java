package com.example.polls.security.service.impl;

import com.example.polls.security.service.LoginAttemptService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.concurrent.ExecutionException;

@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {

    private static final Logger logger = LoggerFactory.getLogger(LoginAttemptServiceImpl.class);

    @Value("${auth.max.failed.login.attempt}")
    private int maxAttempt;

    @Value("${auth.attempt.cache.expire.time.minutes}")
    private int expireMinutes;

    private LoadingCache<String, Integer> attemptsCache;

    @Override
    public void loginSuccessful(String ipAddress) {
        attemptsCache.invalidate(ipAddress);
    }

    @Override
    public void loginFailed(String ipAddress) {
        int attempts;
        try {
            attempts = attemptsCache.get(ipAddress);
        } catch (ExecutionException e) {
            logger.error("Unable to get failed login attempt count from cache for client IP {}", ipAddress);
            attempts = 0;
        }
        attemptsCache.put(ipAddress, ++attempts);
    }

    @Override
    public boolean isBlocked(String ipAddress) {
        try {
            int attempts = attemptsCache.get(ipAddress);
            return attempts >= maxAttempt;
        } catch (ExecutionException e) {
            logger.error("Unable to get failed login attempt count from cache for client IP {}", ipAddress);
        }
        return false;
    }

    @PostConstruct
    private void init() {
        attemptsCache = CacheBuilder.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(expireMinutes))
                .build(CacheLoader.from(this::getDefaultAttemptCount));
    }

    private int getDefaultAttemptCount() {
        return 0;
    }
}
