package com.example.todo.services.implementations;

import com.example.todo.entities.Role;
import com.example.todo.repositories.RoleRepository;
import com.example.todo.services.interfaces.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService implements IRoleService {


    @Autowired
    RoleRepository roleRepository;

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }


}
