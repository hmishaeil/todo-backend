package com.example.todo.services.interfaces;

import com.example.todo.entities.Role;

public interface IRoleService {
    public Role findByName(String name);
}
