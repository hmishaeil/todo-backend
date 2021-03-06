package com.example.todo.services.implementations;

import com.example.todo.entities.PasswordResetToken;
import com.example.todo.exceptions.ResourceNotFoundException;
import com.example.todo.repositories.PasswordResetTokenRepository;
import com.example.todo.services.interfaces.IPasswordResetTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PasswordResetTokenService implements IPasswordResetTokenService {

    @Autowired
    PasswordResetTokenRepository repo;

    @Override
    public PasswordResetToken getByToken(String token) {

        PasswordResetToken entity = repo.findByToken(token);

        if (entity == null) {
            throw new ResourceNotFoundException("Token");
        }

        return entity;
    }

    @Override
    public PasswordResetToken save(PasswordResetToken t) {
        return repo.save(t);
    }
}
