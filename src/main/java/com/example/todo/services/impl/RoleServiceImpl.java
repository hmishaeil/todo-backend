package com.example.todo.services.impl;

import com.example.todo.entities.Role;
import com.example.todo.repositories.RoleRepository;
import com.example.todo.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {


    @Autowired
    RoleRepository roleRepository;

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }


}
