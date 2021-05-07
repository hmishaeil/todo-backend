package com.example.todo.services.impl;

import com.example.todo.entities.ConfirmationTokenEntity;
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
    public ConfirmationTokenEntity save(ConfirmationTokenEntity ct) {

        LOGGER.debug(String.format("Creating ct {}", ct));
        return confirmationTokenRepository.save(ct);
    }

}
