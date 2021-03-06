package com.example.todo.components;

import com.example.todo.entities.Role;
import com.example.todo.entities.Todo;
import com.example.todo.entities.User;
import com.example.todo.repositories.RoleRepository;
import com.example.todo.repositories.TodoRepository;
import com.example.todo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import java.util.Arrays;
import java.util.Date;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup)
            return;

        createRoleIfNotFound("ROLE_ADMIN");
        createRoleIfNotFound("ROLE_SUPPORT");
        createRoleIfNotFound("ROLE_USER");

        createUsersIfNotFound();

        createTodosIfNotFound();

        alreadySetup = true;

    }

    @Transactional
    void createUsersIfNotFound() {

        // Admin users
        for (int i = 1; i <= 3; i++) {
            String username = "admin" + i + "@todo.com";
            User user = userRepository.findByUsernameIgnoreCase(username);
            if (user == null) {
                Role userRole = roleRepository.findByName("ROLE_ADMIN");
                user = new User();
                user.setFirstName("f admin");
                user.setLastName("l admin");
                user.setPassword(passwordEncoder.encode("password"));
                user.setUsername(username);
                user.setRoles(Arrays.asList(userRole));
                user.setEnabled(true);
                user.setVerifiedAt(new Date());
                user.setInternalNote("This is for internal usage. User won't see this message.");
                userRepository.save(user);
            }
        }

        // Support users
        for (int i = 1; i <= 3; i++) {
            String username = "support" + i + "@todo.com";
            User user = userRepository.findByUsernameIgnoreCase(username);
            if (user == null) {
                Role userRole = roleRepository.findByName("ROLE_SUPPORT");
                user = new User();
                user.setFirstName("f support");
                user.setLastName("l support");
                user.setPassword(passwordEncoder.encode("password"));
                user.setUsername(username);
                user.setRoles(Arrays.asList(userRole));
                user.setEnabled(true);
                user.setVerifiedAt(new Date());
                user.setInternalNote("This is for internal usage. User won't see this message.");
                userRepository.save(user);
            }
        }

        // Regular users
        for (int i = 1; i <= 5; i++) {
            String username = "user" + i + "@todo.com";
            User user = userRepository.findByUsernameIgnoreCase(username);
            if (user == null) {
                Role userRole = roleRepository.findByName("ROLE_USER");
                user = new User();
                user.setFirstName("f user");
                user.setLastName("l user");
                user.setPassword(passwordEncoder.encode("password"));
                user.setUsername(username);
                user.setRoles(Arrays.asList(userRole));
                user.setEnabled(true);
                user.setVerifiedAt(new Date());
                userRepository.save(user);
            }
        }
    }

    @Transactional
    void createTodosIfNotFound() {

        for (int i = 1; i <= 5; i++) {
            String username = "user" + i + "@todo.com";
            User user = userRepository.findByUsernameIgnoreCase(username);

            if (todoRepository.getUserTodos(user.getId()).size() == 0) {
                for (int j = 1; j <= 10; j++) {
                    Todo todo = new Todo();
                    todo.setName("Todo " + j + " (userId: " + user.getId() + ")");
                    todo.setDescription("Todo " + j + " sample description");
                    todo.setDone(j % 2 == 0);
                    todo.setTargetDate(new Date());
                    todo.setCreatedBy(user.getId());
                    todo.setUser(user);
                    todoRepository.save(todo);
                }
            }
        }
    }

    @Transactional
    Role createRoleIfNotFound(String name) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role();
            role.setName(name);
            roleRepository.save(role);
        }
        return role;
    }
}
