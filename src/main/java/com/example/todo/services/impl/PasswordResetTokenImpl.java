package com.example.todo.services.impl;

import com.example.todo.entities.PasswordResetToken;
import com.example.todo.exceptions.ResourceNotFoundException;
import com.example.todo.repositories.PasswordResetTokenRepository;
import com.example.todo.services.PasswordResetTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PasswordResetTokenImpl implements PasswordResetTokenService {

    @Autowired
    PasswordResetTokenRepository repo;

    @Override
    public PasswordResetToken getByToken(String token) {
        PasswordResetToken t =  repo.findByToken(token);

        if(t == null){
            throw new ResourceNotFoundException(String.format("%s", "Token Not Found"));
        }

        return t;
    }

    @Override
    public PasswordResetToken save(PasswordResetToken t) {
        return repo.save(t);
    }

}
