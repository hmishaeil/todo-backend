package com.example.todo.services;

import com.example.todo.entities.ConfirmationToken;
import com.example.todo.entities.User;

public interface ConfirmationTokenService {
    public ConfirmationToken save(ConfirmationToken ct);
    public ConfirmationToken findByConfirmationToken(String ct);
    public User getUserByConfirmationToken(String token);
}
