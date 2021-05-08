package com.example.todo.services;

import com.example.todo.entities.ConfirmationToken;

public interface ConfirmationTokenService {
    public ConfirmationToken save(ConfirmationToken ct);
    public ConfirmationToken findByConfirmationToken(String ct);
}