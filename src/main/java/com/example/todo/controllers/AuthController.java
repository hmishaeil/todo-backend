package com.example.todo.controllers;

import com.example.todo.entities.EmailVerification;
import com.example.todo.entities.PasswordResetToken;
import com.example.todo.entities.User;
import com.example.todo.exceptions.EmailNotVerifiedException;
import com.example.todo.exceptions.ResourceAlreadyExistsException;
import com.example.todo.requests.InitResetPasswordRequest;
import com.example.todo.requests.ResetPasswordRequest;
import com.example.todo.requests.SignUpRequest;
import com.example.todo.services.interfaces.IEmailService;
import com.example.todo.services.interfaces.IEmailVerificationService;
import com.example.todo.services.interfaces.IPasswordResetTokenService;
import com.example.todo.services.interfaces.IRoleService;
import com.example.todo.services.interfaces.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.validation.Valid;

import java.util.Arrays;
import java.util.Date;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class AuthController {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    IUserService userService;

    @Autowired
    IRoleService roleService;

    @Autowired
    IEmailVerificationService emailVerificationService;

    @Autowired
    IPasswordResetTokenService passwordResetTokenService;

    @Autowired
    IEmailService emailService;

    @Autowired
    PasswordEncoder encoder;

    @PostMapping("/signup")
    @Transactional
    public User signUp(@Valid @RequestBody SignUpRequest signUpRequest) {

        if(userService.checkIfUserExists(signUpRequest.getUsername())){
            throw new ResourceAlreadyExistsException(String.format("User already exists."));
        }

        User user = modelMapper.map(signUpRequest, User.class);
        user.setRoles(Arrays.asList(roleService.findByName("ROLE_USER")));
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.setEnabled(true);

        userService.create(user);

        EmailVerification ct = new EmailVerification();
        ct.setUser(user);
        emailVerificationService.save(ct);

        emailService.sendEmail("CONFIRMATION_TOKEN", user.getUsername(), ct.getConfirmationToken());

        return user;

    }

    @GetMapping("/verify-email/{ct}")
    @ResponseBody
    public void verifyEmail(@PathVariable String ct) {

        User ut = emailVerificationService.getUserByConfirmationToken(ct);
        ut.setVerifiedAt(new Date());

        userService.update(ut);

    }

    @PostMapping("/request-reset-password")
    @ResponseBody
    public void RequestResetPassword(@RequestBody InitResetPasswordRequest request) {

        User user = userService.getUserByUsername(request.getUsername());

        if (user.getVerifiedAt() == null) {
            throw new EmailNotVerifiedException();
        }

        PasswordResetToken p = new PasswordResetToken();
        p.setUser(user);
        passwordResetTokenService.save(p);

        emailService.sendEmail("RESET_PASSWORD_TOKEN", user.getUsername(), p.getToken());

    }

    @PostMapping("/reset-password")
    @ResponseBody
    public void ResetPassword(@RequestBody ResetPasswordRequest req) {

        PasswordResetToken token = passwordResetTokenService.getByToken(req.getToken());

        User user = userService.getUserByUserId(token.getUser().getId());
        user.setPassword(encoder.encode(req.getPassword()));

        userService.update(user);
    }
}
