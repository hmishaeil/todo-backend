package com.example.todo.repositories;

import com.example.todo.entities.EmailVerification;
import com.example.todo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
    EmailVerification findByConfirmationToken(String ct);
    User getUser(String token);
}
