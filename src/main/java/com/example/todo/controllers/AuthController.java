package com.example.todo.controllers;

import com.example.todo.components.AppComponent;
import com.example.todo.entities.ConfirmationTokenEntity;
import com.example.todo.entities.UserEntity;
import com.example.todo.requests.SignUpRequest;
import com.example.todo.services.ConfirmationTokenService;
import com.example.todo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class AuthController {

    @Autowired
    UserService userService;

    @Autowired
    ConfirmationTokenService confirmationTokenService;

    @Autowired
    AppComponent appComponent;

    @PostMapping("/signup")
    public UserEntity signUp(@Valid @RequestBody SignUpRequest signUpRequest) {

        String password = signUpRequest.getPassword();

        UserEntity userEntity = appComponent.signUpModelMapper(password).map(signUpRequest, UserEntity.class);

        userEntity.setConfirmationTokenEntity(new ConfirmationTokenEntity());

        return userService.save(userEntity);
    }

}
