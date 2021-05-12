package com.example.todo.services.impl;

import com.example.todo.entities.ConfirmationToken;
import com.example.todo.entities.User;
import com.example.todo.exceptions.ResourceNotFoundException;
import com.example.todo.repositories.ConfirmationTokenRepository;
import com.example.todo.services.ConfirmationTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;

    @Override
    public ConfirmationToken save(ConfirmationToken ct) {

        LOGGER.debug(String.format("Creating ct {}", ct));
        return confirmationTokenRepository.save(ct);
    }

    @Override
    public ConfirmationToken findByConfirmationToken(String ct) {
        ConfirmationToken entity = confirmationTokenRepository.findByConfirmationToken(ct);
        if (entity == null) {
            throw new ResourceNotFoundException(String.format("%s", "Token not found."));
        }

        return entity;
    }

    @Override
    public User getUserByConfirmationToken(String token) {
        User user = confirmationTokenRepository.getUser(token);
        return user;
    }

}
