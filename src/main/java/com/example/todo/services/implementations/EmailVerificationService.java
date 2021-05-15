package com.example.todo.services.implementations;

import com.example.todo.entities.EmailVerification;
import com.example.todo.entities.User;
import com.example.todo.exceptions.ResourceNotFoundException;
import com.example.todo.repositories.EmailVerificationRepository;
import com.example.todo.services.interfaces.IEmailVerificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailVerificationService implements IEmailVerificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    EmailVerificationRepository emailVerificationRepository;

    @Override
    public EmailVerification save(EmailVerification ct) {

        LOGGER.debug(String.format("Creating ct {}", ct));
        return emailVerificationRepository.save(ct);
    }

    @Override
    public EmailVerification findByConfirmationToken(String ct) {
        EmailVerification entity = emailVerificationRepository.findByConfirmationToken(ct);
        if (entity == null) {
            throw new ResourceNotFoundException(String.format("%s", "Token not found."));
        }

        return entity;
    }

    @Override
    public User getUserByConfirmationToken(String token) {
        User user = emailVerificationRepository.getUser(token);

        if (user == null) {
            throw new ResourceNotFoundException("Token");
        }
        return user;
    }

}
