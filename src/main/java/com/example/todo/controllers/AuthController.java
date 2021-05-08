package com.example.todo.controllers;

import com.example.todo.components.AppComponent;
import com.example.todo.entities.ConfirmationToken;
import com.example.todo.entities.User;
import com.example.todo.requests.SignUpRequest;
import com.example.todo.services.AwsSesService;
import com.example.todo.services.ConfirmationTokenService;
import com.example.todo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Autowired
    AwsSesService awsSesService;

    @PostMapping("/signup")
    public User signUp(@Valid @RequestBody SignUpRequest signUpRequest) {

        String password = signUpRequest.getPassword();

        User userEntity = appComponent.signUpModelMapper(password).map(signUpRequest, User.class);

        userEntity.setConfirmationToken(new ConfirmationToken());

        awsSesService.sendEmail("hmishaeil@gmail.com", "body");
        return userService.create(userEntity);
    }

    @GetMapping("/verify-email/{ct}")
    public ResponseEntity<String> verifyEmail(@PathVariable String ct) {

        User ut = userService.joinUsersByConfirmationTokens(ct);
        ut.setEmailVerified(true);

        userService.update(ut);
        
        return ResponseEntity.ok("Email verified successfully.");
    }

}
