package com.example.todo.controllers;

import com.example.todo.entities.EmailVerification;
import com.example.todo.entities.PasswordResetToken;
import com.example.todo.entities.User;
import com.example.todo.exceptions.EmailNotVerifiedException;
import com.example.todo.exceptions.LoginAttemptsExceededException;
import com.example.todo.exceptions.ResourceAlreadyExistsException;
import com.example.todo.jwt.JwtTokenUtil;
import com.example.todo.requests.InitResetPasswordRequest;
import com.example.todo.requests.LoginRequest;
import com.example.todo.requests.ResetPasswordRequest;
import com.example.todo.requests.SignUpRequest;
import com.example.todo.responses.JwtTokenResponse;
import com.example.todo.responses.LoginResponse;
import com.example.todo.services.implementations.LoginAttemptCacheService;
import com.example.todo.services.interfaces.IEmailService;
import com.example.todo.services.interfaces.IEmailVerificationService;
import com.example.todo.services.interfaces.IPasswordResetTokenService;
import com.example.todo.services.interfaces.IRoleService;
import com.example.todo.services.interfaces.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import java.util.Arrays;
import java.util.Date;

@CrossOrigin(origins = "*")
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

  @Autowired
  JwtTokenUtil jwtTokenUtil;

  @Autowired
  private LoginAttemptCacheService loginAttemptCacheService;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Value("${jwt.http.request.header}")
  private String tokenHeader;

  @PostMapping("/signup")
  @Transactional
  public User signUp(@Valid @RequestBody SignUpRequest signUpRequest) {

    if (userService.checkIfUserExists(signUpRequest.getUsername())) {
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

  @GetMapping("/validate-jwt/{token}/{username}")
  @ResponseBody
  public Boolean validateJwtToken(@PathVariable String token, @PathVariable String username) {

    Boolean result;

    try {
      result = jwtTokenUtil.validateToken(token, username);
    } catch (Exception e) {
      result = false;
    }

    return result;
  }

  // Login
  @PostMapping(value = "${jwt.get.token.uri}")
  public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody LoginRequest loginRequest) {

    if (loginAttemptCacheService.checkIfLoginAttemptsExceeded(loginRequest.getUsername())) {
      throw new LoginAttemptsExceededException();
    }

    String username = loginRequest.getUsername();
    String password = loginRequest.getPassword();

    Authentication authenticatedUser = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(username, password));

    final User user = (User) authenticatedUser.getPrincipal();
    if (user.getVerifiedAt() == null) {
      throw new EmailNotVerifiedException(String.format("Email not verified yet. '%s'.", user.getUsername()));
    }

    LoginResponse response = new LoginResponse();
    response.setUsername(user.getUsername());
    response.setToken(jwtTokenUtil.generateToken(user));
    response.setRole(user.getRoles().iterator().next().getName());
    response.setUserId(user.getId());

    return ResponseEntity.ok(response);
  }

  // Refresh Token
  @GetMapping(value = "${jwt.refresh.token.uri}")
  public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
    String authToken = request.getHeader(tokenHeader);
    final String token = authToken.substring(7);
    if (jwtTokenUtil.canTokenBeRefreshed(token)) {
      String refreshedToken = jwtTokenUtil.refreshToken(token);
      return ResponseEntity.ok(new JwtTokenResponse(refreshedToken));
    } else {
      return ResponseEntity.badRequest().body(null);
    }
  }

}
