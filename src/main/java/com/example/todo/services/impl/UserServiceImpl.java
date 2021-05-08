package com.example.todo.services.impl;

import com.example.todo.entities.ConfirmationToken;
import com.example.todo.entities.User;
import com.example.todo.exceptions.ResourceAlreadyExistsException;
import com.example.todo.exceptions.ResourceNotFoundException;
import com.example.todo.repositories.ConfirmationTokenRepository;
import com.example.todo.repositories.UserRepository;
import com.example.todo.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public User create(final User user) {
        LOGGER.debug("Creating {}", user);

        User existing = userRepository.findByEmailIgnoreCase(user.getEmail());
        if(existing != null){
            throw new ResourceAlreadyExistsException(String.format("User already exists."));
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getList() {
        return userRepository.findAll();
    }
 

    @Override
    public User joinUsersByConfirmationTokens(String token) {
         
        User ue = userRepository.joinByConfirmationTokens(token);
        if(ue == null){
            throw new ResourceNotFoundException(String.format("%s", "Confirmation token not found."));
        }
        return ue;
    }

    @Override
    public  User update(User ue) {
        return userRepository.save(ue);
    }
 

}
