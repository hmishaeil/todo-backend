package com.example.todo.services.interfaces;

import com.example.todo.entities.EmailVerification;
import com.example.todo.entities.User;

public interface IEmailVerificationService {
    public EmailVerification save(EmailVerification ct);
    public EmailVerification findByConfirmationToken(String ct);
    public User getUserByConfirmationToken(String token);
}
