package com.example.todo.services.interfaces;

import com.example.todo.entities.PasswordResetToken;

public interface IPasswordResetTokenService {
    public PasswordResetToken getByToken(String token);
    public PasswordResetToken save(PasswordResetToken t);
}
