package com.example.todo.jwt;

import com.example.todo.entities.User;
import com.example.todo.exceptions.EmailNotVerifiedException;
import com.example.todo.services.implementations.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserService userService;

    @Override
    public User loadUserByUsername(String username) {
        User user = userService.getUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("User not found. '%s'.", username));
        }

        if (user.getVerifiedAt() == null) {
            throw new EmailNotVerifiedException(String.format("Email not verified yet. '%s'.", username));
        }

        return user;
    }

    // private Collection<GrantedAuthority> getAuthorities(Collection<Role> roles) {
    // List<GrantedAuthority> authorities = new ArrayList<>();
    // for (Role role : roles) {
    // authorities.add(new SimpleGrantedAuthority(role.getName()));
    // // role.getPrivileges().stream().map(p -> new
    // // SimpleGrantedAuthority(p.getName())).forEach(authorities::add);
    // }

    // return authorities;
    // }

}

