package com.example.todo.services.interfaces;

import com.example.todo.entities.User;

import java.util.List;

public interface IUserService {
    public Boolean checkIfUserExists(String username);
    public User getUserByUsername(String username);
    public User getUserByUserId(Long id);
    public User create(User user);
    public User update(User user);
    public List<User> getUsers();
}
