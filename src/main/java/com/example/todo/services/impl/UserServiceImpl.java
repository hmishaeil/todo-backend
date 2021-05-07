package com.example.todo.services.impl;

import com.example.todo.entities.UserEntity;
import com.example.todo.exceptions.ResourceAlreadyExistsException;
import com.example.todo.repositories.ConfirmationTokenRepository;
import com.example.todo.repositories.UserRepository;
import com.example.todo.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    
    @Autowired
    UserRepository userRepository;

    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;

    @Override
    @Transactional
    public UserEntity save(final UserEntity user) {
        LOGGER.debug("Creating {}", user);

        UserEntity existing = userRepository.findByEmailIgnoreCase(user.getEmail());
        if(existing != null){
            throw new ResourceAlreadyExistsException(String.format("User already exists."));
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserEntity> getList() {
        return userRepository.findAll();
    }

}
