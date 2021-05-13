package com.example.todo.controllers;

import com.example.todo.entities.User;
import com.example.todo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    
    @Autowired
    UserService userService;

    @GetMapping("/users")
    @ResponseBody
    public List<User> getUsers(){
        return userService.getUsers();
    }
}
