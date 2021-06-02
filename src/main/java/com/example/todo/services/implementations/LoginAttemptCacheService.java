package com.example.todo.services.implementations;

import com.example.todo.services.interfaces.ILoginAttemptCacheService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalCause;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class LoginAttemptCacheService implements ILoginAttemptCacheService {

    private final int MAX_ATTEMPT = 5;
    private int EXPIRE_IN = 15;

    private LoadingCache<String, Integer> loginAttemptsCache;

    @PostConstruct
    void init() {
        loginAttemptsCache = CacheBuilder.newBuilder().expireAfterWrite(EXPIRE_IN, TimeUnit.MINUTES)
                .build(new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(String key) {
                        return 0;
                    }
                });
    }

    @Override
    public void loginFailed(String username) {

        int attempts = 0;

        try {
            attempts = loginAttemptsCache.get(username);
        } catch (ExecutionException e) {
            attempts = 0;
        }

        attempts++;

        loginAttemptsCache.put(username, attempts);

        log.warn(String.format("Login failed for %s (%s)", username, attempts));

    }

    @Override
    public void loginSucceeded(String username) {
        loginAttemptsCache.invalidate(username);
    }

    @Override
    public boolean checkIfLoginAttemptsExceeded(String username) {
        try {
            return loginAttemptsCache.get(username) >= MAX_ATTEMPT;
        } catch (ExecutionException e) {
            return false;
        }
    }

}
