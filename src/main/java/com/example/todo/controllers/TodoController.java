package com.example.todo.controllers;

import com.example.todo.entities.Todo;
import com.example.todo.entities.User;
import com.example.todo.requests.TodoDto;
import com.example.todo.services.ToDoService;
import com.example.todo.services.UserService;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin("http://localhost:4200")
public class TodoController {

    @Autowired
    ToDoService todoService;

    @Autowired
    UserService userService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/todos")
    @ResponseBody
    public List<Todo> getTodos(Authentication authentication) {

        log.info("{}", authentication.getAuthorities().toString().contains("ADMIN"));

        if (authentication.getAuthorities().toString().contains("ADMIN")) {
            return todoService.getTodos();
        }

        User user = userService.getUserByUsername(authentication.getName());
        return todoService.getTodosForUser(user.getId());

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
