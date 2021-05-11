package com.example.todo.services;

import com.example.todo.entities.PasswordResetToken;
import com.example.todo.entities.User;

public interface PasswordResetTokenService {
    public PasswordResetToken getByToken(String token);
    public PasswordResetToken save(PasswordResetToken t);
}
