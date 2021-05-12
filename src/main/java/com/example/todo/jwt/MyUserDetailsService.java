package com.example.todo.jwt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.example.todo.entities.Privilege;
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

    if (userEntity.getVerified_at() == null) {
      throw new EmailNotVerifiedException(String.format("EMAIL_NOT_VERIFIED_YET '%s'.", username));
    }

    // UserDetails userDetails = new MyUserDetails(userEntity.getId(),
    // userEntity.getUsername(), userEntity.getPassword(), "ROLE_USER_2");
    // UserDetails userDetails = new MyUserDetails(userEntity.getId(),
    // userEntity.getUsername(), userEntity.getPassword(),
    // getAuthorities(Arrays.asList(
    // roleRepository.findByName("ROLE_USER"))));

    // return userDetails;

    return new org.springframework.security.core.userdetails.User(userEntity.getEmail(), userEntity.getPassword(),
        userEntity.isEnabled(), true, true, true, getAuthorities(userEntity.getRoles()));
  }

  private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {

    return getGrantedAuthorities(getPrivileges(roles));
  }

  private List<String> getPrivileges(Collection<Role> roles) {

    List<String> privileges = new ArrayList<>();
    List<Privilege> collection = new ArrayList<>();
    for (Role role : roles) {
      collection.addAll(role.getPrivileges());
    }
    for (Privilege item : collection) {
      privileges.add(item.getName());
    }
    return privileges;
  }

  private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
    List<GrantedAuthority> authorities = new ArrayList<>();
    for (String privilege : privileges) {
      authorities.add(new SimpleGrantedAuthority(privilege));
    }
    return authorities;
  }

}