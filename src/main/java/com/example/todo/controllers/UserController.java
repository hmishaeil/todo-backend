package com.example.todo.controllers;

import com.example.todo.entities.User;
import com.example.todo.services.interfaces.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    IUserService userService;

    @GetMapping("/users")
    @ResponseBody
    @Secured({ "ROLE_SUPERADMIN", "ROLE_ADMIN", "ROLE_SUPPORT" })
    public List<User> getUsers(Authentication authentication) {

        log.info("{}", authentication);

        return userService.getUsers();
    }

    @Secured({ "ROLE_SUPERADMIN", "ROLE_ADMIN", "ROLE_SUPPORT" })
    @GetMapping("/users/{id}")
    @ResponseBody
    public User getUser(@PathVariable Long id) {
        return userService.getUserByUserId(id);
    }

    @Secured({ "ROLE_SUPERADMIN", "ROLE_ADMIN" })
    @PutMapping("/users")
    @ResponseBody
    public User updateUser(@RequestBody User user) {
        return userService.update(user);
    }
}
