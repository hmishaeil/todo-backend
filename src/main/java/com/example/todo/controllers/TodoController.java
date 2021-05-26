package com.example.todo.controllers;

import com.example.todo.entities.Todo;
import com.example.todo.entities.User;
import com.example.todo.requests.TodoDto;
import com.example.todo.services.interfaces.IToDoService;
import com.example.todo.services.interfaces.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
public class TodoController {

    @Autowired
    IToDoService todoService;

    @Autowired
    IUserService userService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/todos")
    @ResponseBody
    public List<Todo> getTodos(Authentication authentication, @RequestParam Optional<String> searchTerm) {

        List<Todo> todos;

        if (authentication.getAuthorities().toString().contains("ADMIN")) {
            todos = todoService.getTodos();
        } else {
            User user = userService.getUserByUsername(authentication.getName());
            todos = todoService.getTodosForUser(user.getId());
        }

        if (searchTerm.isPresent()) {
            todos = Arrays
                    .asList(todos.stream()
                            .filter(todo -> todo.getName().toLowerCase().contains(searchTerm.get())
                                    || todo.getDescription().toLowerCase().contains(searchTerm.get()))
                            .toArray(Todo[]::new));
        }

        return todos;
    }

    @GetMapping("/todos/{id}")
    public ResponseEntity<Todo> getTodo(@PathVariable Long id) {
        return ResponseEntity.ok(todoService.getTodo(id));
    }

    @PostMapping("/todos")
    public Todo createTodo(@RequestBody TodoDto td, Principal p) {

        User user = userService.getUserByUsername(p.getName());

        Todo todo = modelMapper.map(td, Todo.class);
        todo.setUser(user);

        Todo createdTodo = todoService.createTodo(todo);

        return createdTodo;
    }

    @PutMapping("/todos")
    public Todo updateTodo(@RequestBody TodoDto td, Principal p) {

        Todo todo = todoService.getTodo(td.getId());

        todo.setDescription(td.description);
        todo.setDone(td.done);
        todo.setName(td.name);
        todo.setTargetDate(td.targetDate);

        return todoService.updateTodo(todo);
    }

    @DeleteMapping("/todos/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        return todoService.deleteTodo(id);
    }
}
