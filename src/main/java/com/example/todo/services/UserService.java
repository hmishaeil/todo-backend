package com.example.todo.services;

import com.example.todo.entities.UserEntity;

import java.util.List;

public interface UserService {
    public UserEntity save(UserEntity user);
    public List<UserEntity> getList();
}
