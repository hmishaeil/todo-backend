package com.example.todo.controllers;

import com.example.todo.entities.Role;
import com.example.todo.entities.User;
import com.example.todo.exceptions.ResourceAlreadyExistsException;
import com.example.todo.requests.AddUserRequest;
import com.example.todo.requests.UpdateUserRequest;
import com.example.todo.services.interfaces.IEmailService;
import com.example.todo.services.interfaces.IRoleService;
import com.example.todo.services.interfaces.IToDoService;
import com.example.todo.services.interfaces.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    IUserService userService;

    @Autowired
    IRoleService roleService;

    @Autowired
    IToDoService todoService;

    @Autowired
    IEmailService emailService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PasswordEncoder encoder;

    @GetMapping("/users")
    @ResponseBody
    @Secured({ "ROLE_ADMIN", "ROLE_SUPPORT" })
    public List<User> getUsers(Authentication authentication, @RequestParam Optional<String> searchTerm) {

        log.info("{}", authentication);

        List<User> users = null;
        users = userService.getUsers();

        if (searchTerm.isPresent()) {
            users = Arrays.asList(users.stream()
                    .filter(user -> user.getUsername().toLowerCase().contains(searchTerm.get())
                            || user.getRoles().iterator().next().getName().toLowerCase().contains(searchTerm.get()))
                    .toArray(User[]::new));
        }

        return users;
    }

    @Secured({ "ROLE_ADMIN", "ROLE_SUPPORT" })
    @GetMapping("/users/{id}")
    @ResponseBody
    public User getUser(@PathVariable Long id) {
        return userService.getUserByUserId(id);
    }

    @Secured({ "ROLE_ADMIN" })
    @PostMapping("/users")
    @ResponseBody
    public User addUser(@RequestBody AddUserRequest user, Authentication authentication) {

        if (userService.checkIfUserExists(user.getUsername())) {
            throw new ResourceAlreadyExistsException("Username already exists.");
        }

        User creator = userService.getUserByUsername(authentication.getName());
        User newUser = modelMapper.map(user, User.class);

        newUser.setCreatedAt(new Date());
        newUser.setCreatedBy(creator.getId());
        newUser.setEnabled(true);
        newUser.setVerifiedAt(new Date());

        String tempPass = RandomStringUtils.randomAlphanumeric(8);
        log.info(tempPass);

        emailService.sendEmail("ADD_USER", newUser.getUsername(), tempPass);

        newUser.setPassword(encoder.encode(tempPass));

        Role role = roleService.findByName("ROLE_" + user.getRole());
        Collection<Role> roles = new ArrayList<>();
        roles.add(role);
        newUser.setRoles(roles);

        return userService.create(newUser);

    }

    @Secured({ "ROLE_ADMIN" })
    @PutMapping("/users")
    @ResponseBody
    public User updateUser(@RequestBody UpdateUserRequest req) {

        User user = modelMapper.map(req, User.class);
        return userService.update(user);
    }

}
