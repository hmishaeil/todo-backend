package com.example.todo.services.interfaces;

public interface ILoginAttemptCacheService {
    
    public void loginFailed(String username);
    public void loginSucceeded(String username);
    public boolean checkIfLoginAttemptsExceeded(String username);
}
