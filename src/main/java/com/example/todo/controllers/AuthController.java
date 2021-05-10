package com.example.todo.controllers;

import com.example.todo.components.AppComponent;
import com.example.todo.entities.ConfirmationToken;
import com.example.todo.entities.User;
import com.example.todo.jwt.JwtTokenUtil;
import com.example.todo.jwt.resource.JwtTokenRequest;
import com.example.todo.jwt.resource.JwtTokenResponse;
import com.example.todo.requests.SignUpRequest;
import com.example.todo.requests.ValidateJwtTokenRequest;
import com.example.todo.responses.ValidateJwtTokenResponse;
import com.example.todo.services.AwsSesService;
import com.example.todo.services.ConfirmationTokenService;
import com.example.todo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class AuthController {

    @Autowired
    JwtTokenUtil jwtUtil;

    @Autowired
    private UserDetailsService jwtInMemoryUserDetailsService;

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

        ConfirmationToken ct = new ConfirmationToken();
        userEntity.setConfirmationToken(ct);

        awsSesService.sendEmail(userEntity.getEmail(), ct.getConfirmationToken());

        return userService.create(userEntity);
    }

    @GetMapping("/verify-email/{ct}")
    public ResponseEntity<String> verifyEmail(@PathVariable String ct) {

        User ut = userService.joinUsersByConfirmationTokens(ct);
        ut.setEmailVerified(true);

        userService.update(ut);

        Path fileName = Path.of("src/main/resources/templates/verify_email/email_verified.html");
        String actual = null;
        try {
            actual = Files.readString(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(actual);
    }

    @PostMapping("/validate-jwt")
    public ResponseEntity<ValidateJwtTokenResponse> validateJwt(@RequestBody ValidateJwtTokenRequest request) {

        UserDetails userDetails = jwtInMemoryUserDetailsService.loadUserByUsername(request.username);
        Boolean validationResult = jwtUtil.validateToken(request.token, userDetails);

        ValidateJwtTokenResponse response = new ValidateJwtTokenResponse();
        response.valid = validationResult;

        return ResponseEntity.ok(response);
    }

}
