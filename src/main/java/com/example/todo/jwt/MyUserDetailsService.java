package com.example.todo.jwt;

import com.example.todo.entities.Role;
import com.example.todo.entities.User;
import com.example.todo.exceptions.EmailNotVerifiedException;
import com.example.todo.repositories.RoleRepository;
import com.example.todo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {

  static {
  }

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    User userEntity = userRepository.findByUsernameIgnoreCase(username);

    if (userEntity == null) {
      throw new UsernameNotFoundException(String.format("USER_NOT_FOUND '%s'.", username));
    }

    if (userEntity.getVerifiedAt() == null) {
      throw new EmailNotVerifiedException(String.format("EMAIL_NOT_VERIFIED_YET '%s'.", username));
    }

    return new org.springframework.security.core.userdetails.User(userEntity.getUsername(), userEntity.getPassword(),
        userEntity.isEnabled(), true, true, true, getAuthorities(userEntity.getRoles()));
  }

  private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {

    return getGrantedAuthorities(roles);
  }

  private List<GrantedAuthority> getGrantedAuthorities(Collection<Role> roles) {
    List<GrantedAuthority> authorities = new ArrayList<>();

    for (Role role : roles) {
      authorities.add(new SimpleGrantedAuthority(role.getName()));
    }

    return authorities;
  }
}
