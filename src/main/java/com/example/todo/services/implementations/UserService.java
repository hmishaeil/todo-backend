package com.example.todo.services.implementations;

import com.example.todo.entities.User;
import com.example.todo.exceptions.ResourceNotFoundException;
import com.example.todo.repositories.UserRepository;
import com.example.todo.services.interfaces.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserRepository userRepository;

    @Override
    public User create(final User user) {
        LOGGER.info("Creating {}", user);
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User update(User ue) {

        User user = userRepository.findByUsernameIgnoreCase(ue.getUsername());
        if (user == null) {
            throw new ResourceNotFoundException(String.format("%s", "User not found."));
        }

        user.setEnabled(ue.isEnabled());
        user.setInternalNote(ue.getInternalNote());

        return userRepository.save(user);
    }

    @Override
    public User getUserByUsername(String username) {

        User user = userRepository.findByUsernameIgnoreCase(username);

        if (user == null) {
            throw new ResourceNotFoundException("User");
        }

        return user;
    }

    @Override
    public User getUserByUserId(Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new ResourceNotFoundException(String.format("%s", "User not found."));
        }

        return user.get();
    }

    @Override
    public Boolean checkIfUserExists(String username) {
        return userRepository.findByUsernameIgnoreCase(username) != null ? true : false;
    }

}
