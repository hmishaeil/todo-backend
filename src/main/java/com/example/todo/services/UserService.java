package com.example.todo.services;

import com.example.todo.entities.User;

import java.util.List;

public interface UserService {
    public User create(User user);
    public User update(User user);
    public List<User> getList();
    public User joinUsersByConfirmationTokens(String token);
}
