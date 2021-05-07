package com.example.todo.repositories;

import com.example.todo.entities.ConfirmationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationTokenEntity, Long> {
    ConfirmationTokenEntity findByConfirmationToken(String ct);
}
