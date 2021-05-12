package com.example.todo.repositories;

import com.example.todo.entities.ConfirmationToken;
import com.example.todo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    ConfirmationToken findByConfirmationToken(String ct);
    User getUser(String token);
}
