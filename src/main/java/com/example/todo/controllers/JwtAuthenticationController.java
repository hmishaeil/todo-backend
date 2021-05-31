package com.example.todo.controllers;

import com.example.todo.entities.User;
import com.example.todo.exceptions.AuthenticationException;
import com.example.todo.exceptions.EmailNotVerifiedException;
import com.example.todo.jwt.JwtTokenUtil;
import com.example.todo.requests.JwtTokenRequest;
import com.example.todo.responses.JwtTokenResponse;
import com.example.todo.responses.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import java.util.Objects;

@RestController
@CrossOrigin(origins = "*")
public class JwtAuthenticationController {

  @Value("${jwt.http.request.header}")
  private String tokenHeader;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  // login
  @RequestMapping(value = "${jwt.get.token.uri}", method = RequestMethod.POST)
  public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtTokenRequest authenticationRequest)
      throws AuthenticationException {

    Authentication authenticatedUser = authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

    final User user = (User)authenticatedUser.getPrincipal();
    if(user.getVerifiedAt() == null){
        throw new EmailNotVerifiedException(String.format("Email not verified yet. '%s'.", user.getUsername()));
    }


    final String token = jwtTokenUtil.generateToken(user);

    LoginResponse response = new LoginResponse();
    response.setUsername(user.getUsername());
    response.setToken(token);
    response.setRole(user.getRoles().iterator().next().getName());
    response.setUserId(user.getId());

    return ResponseEntity.ok(response);
  }

  @RequestMapping(value = "${jwt.refresh.token.uri}", method = RequestMethod.GET)
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

  private Authentication authenticate(String username, String password) {

    Objects.requireNonNull(username);
    Objects.requireNonNull(password);

    return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
  }
}