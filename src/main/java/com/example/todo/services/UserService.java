package com.example.todo.services;

import com.example.todo.entities.User;

import java.util.List;

public interface UserService {
    public User getUserByUsername(String username);
    public User getUserByUserId(Long id);
    public User create(User user);
    public User update(User user);
    public List<User> getUsers();
}
