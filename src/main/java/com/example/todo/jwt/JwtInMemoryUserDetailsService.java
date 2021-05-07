package com.example.todo.jwt;

import java.util.ArrayList;
import java.util.List;

import com.example.todo.entities.UserEntity;
import com.example.todo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtInMemoryUserDetailsService implements UserDetailsService {

  static List<JwtUserDetails> inMemoryUserList = new ArrayList<>();

  static {
    // inMemoryUserList.add(new JwtUserDetails(5L, "u5",
    // "$2a$10$DJEMlW7FrNq5tMk2obJIqOa572nXkA8IHFH/dyBpeW6dwTJtFh10i",
    // "ROLE_USER_2"));
  }

  @Autowired
  UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // Optional<JwtUserDetails> findFirst = inMemoryUserList.stream().filter(user ->
    // user.getUsername().equals(username)).findFirst();

    UserEntity userEntity = userRepository.findByUsernameIgnoreCase(username);

    if (userEntity == null) {
      throw new UsernameNotFoundException(String.format("USER_NOT_FOUND '%s'.", username));
    }

    UserDetails userDetails = new JwtUserDetails(userEntity.getUserId(), userEntity.getUsername(),
        userEntity.getPassword(), "ROLE_USER_2");

    return userDetails;
  }

}