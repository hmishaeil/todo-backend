package com.example.todo.services;

import com.example.todo.entities.Role;

public interface RoleService {
    public Role findByName(String name);
}
