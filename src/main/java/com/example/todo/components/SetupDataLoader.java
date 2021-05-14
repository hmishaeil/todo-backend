package com.example.todo.components;

import com.example.todo.entities.Privilege;
import com.example.todo.entities.Role;
import com.example.todo.entities.Todo;
import com.example.todo.entities.User;
import com.example.todo.repositories.PrivilegeRepository;
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
import java.util.Collection;
import java.util.Date;
import java.util.List;

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
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup)
            return;

        Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        List<Privilege> adminPrivileges = Arrays.asList(readPrivilege, writePrivilege);
        createRoleIfNotFound("ROLE_ROOT", Arrays.asList(readPrivilege));
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_SUPPORT", Arrays.asList(readPrivilege));
        createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege));

        createUsersIfNotFound();

        createTodosIfNotFound();

        alreadySetup = true;

    }

    @Transactional
    void createUsersIfNotFound() {

        // Root users
        for (int i = 1; i <= 2; i++) {
            String username = "root" + i + "@cashmino.com";
            User user = userRepository.findByUsernameIgnoreCase(username);
            if (user == null) {
                Role userRole = roleRepository.findByName("ROLE_ROOT");
                user = new User();
                user.setFirstName("f root");
                user.setLastName("l root");
                user.setPassword(passwordEncoder.encode("password"));
                user.setUsername(username);
                user.setRoles(Arrays.asList(userRole));
                user.setEnabled(true);
                user.setVerifiedAt(new Date());
                userRepository.save(user);
            }
        }

        // Admin users
        for (int i = 1; i <= 3; i++) {
            String username = "admin" + i + "@cashmino.com";
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
        for (int i = 1; i <= 5; i++) {
            String username = "support" + i + "@cashmino.com";
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
        for (int i = 1; i <= 10; i++) {
            String username = "user" + i + "@cashmino.com";
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

        for (int i = 1; i <= 10; i++) {
            String username = "user" + i + "@cashmino.com";
            User user = userRepository.findByUsernameIgnoreCase(username);

            if (todoRepository.getUserTodos(user.getId()).size() == 0) {
                for (int j = 1; j <= 10; j++) {
                    Todo todo = new Todo();
                    todo.setName("Todo " + j + " (userId: " + user.getId() + ")");
                    todo.setDescription("Todo " + j + " sample description");
                    todo.setDone(j % 2 == 0);
                    todo.setTargetDate(new Date());
                    todo.setUser(user);
                    todoRepository.save(todo);
                }
            }
        }
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege();
            privilege.setName(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role();
            role.setName(name);
            // role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }
}
