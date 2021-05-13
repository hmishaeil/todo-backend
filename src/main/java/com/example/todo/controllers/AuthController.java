package com.example.todo.controllers;

import com.example.todo.components.AppComponent;
import com.example.todo.entities.ConfirmationToken;
import com.example.todo.entities.PasswordResetToken;
import com.example.todo.entities.User;
import com.example.todo.jwt.JwtTokenUtil;
import com.example.todo.requests.InitResetPasswordRequest;
import com.example.todo.requests.ResetPasswordRequest;
import com.example.todo.requests.SignUpRequest;
import com.example.todo.requests.ValidateJwtTokenRequest;
import com.example.todo.responses.ValidateJwtTokenResponse;
import com.example.todo.services.AwsSesService;
import com.example.todo.services.ConfirmationTokenService;
import com.example.todo.services.PasswordResetTokenService;
import com.example.todo.services.RoleService;
import com.example.todo.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Date;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class AuthController {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    JwtTokenUtil jwtUtil;

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    ConfirmationTokenService confirmationTokenService;

    @Autowired
    PasswordResetTokenService passwordResetTokenService;

    @Autowired
    AppComponent appComponent;

    @Autowired
    AwsSesService awsSesService;

    @Autowired
    PasswordEncoder encoder;

    @PostMapping("/signup")
    public User signUp(@Valid @RequestBody SignUpRequest signUpRequest) {

        User user = modelMapper.map(signUpRequest, User.class);
        user.setRoles(Arrays.asList(roleService.findByName("ROLE_USER")));
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.setEnabled(true);
        
        userService.create(user);

        ConfirmationToken ct = new ConfirmationToken();
        ct.setUser(user);
        confirmationTokenService.save(ct);

        awsSesService.sendEmail(user.getEmail(), ct.getConfirmationToken());

        return user;

    }

    @GetMapping("/verify-email/{ct}")
    public ResponseEntity<String> verifyEmail(@PathVariable String ct) {

        User ut = confirmationTokenService.getUserByConfirmationToken(ct);
        ut.setVerified_at(new Date());

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

    @PostMapping("/request-reset-password")
    public ResponseEntity<Void> RequestResetPassword(@RequestBody InitResetPasswordRequest request) {

        User u = userService.getUserByUsername(request.getUsername());

        PasswordResetToken p = new PasswordResetToken();
        p.setUser(u);
        passwordResetTokenService.save(p);

        awsSesService.sendResetEmail(u.getEmail(), p.getToken());
        ValidateJwtTokenResponse response = new ValidateJwtTokenResponse();
        response.valid = true;
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    @ResponseBody
    public void ResetPassword(@RequestBody ResetPasswordRequest req) {
        PasswordResetToken entity = passwordResetTokenService.getByToken(req.getToken());
        User user = userService.getUserByUserId(entity.getUser().getId());
        user.setPassword(encoder.encode(req.getPassword()));
        userService.update(user);
    }
}
