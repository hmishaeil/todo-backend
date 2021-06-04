package com.example.todo.controllers;

import com.example.todo.entities.Todo;
import com.example.todo.entities.User;
import com.example.todo.requests.TodoDto;
import com.example.todo.services.interfaces.IToDoService;
import com.example.todo.services.interfaces.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@PreAuthorize("#userId == authentication.principal.id or hasRole('ROLE_ADMIN')")
public class TodoController {

    @Autowired
    IToDoService todoService;

    @Autowired
    IUserService userService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/users/{userId}/todos")
    @ResponseBody
    public List<Todo> getTodos(@PathVariable Long userId, @RequestParam Optional<String> searchTerm) {

        List<Todo> todos = null;

        todos = todoService.getTodosForUser(userId);

        if (searchTerm.isPresent()) {
            todos = Arrays
                    .asList(todos.stream()
                            .filter(todo -> todo.getName().toLowerCase().contains(searchTerm.get())
                                    || todo.getDescription().toLowerCase().contains(searchTerm.get()))
                            .toArray(Todo[]::new));
        }

        return todos;
    }

    @GetMapping("/users/{userId}/todos/{todoId}")
    public ResponseEntity<Todo> getTodo(@PathVariable Long userId, @PathVariable Long todoId) {
        return ResponseEntity.ok(todoService.getTodo(todoId));
    }

    @PostMapping("/users/{userId}/todos")
    public Todo createTodo(@RequestBody TodoDto td, @PathVariable Long userId) {

        User user = userService.getUserByUserId(userId);
        Long loggedInUserId = ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        
        Todo todo = modelMapper.map(td, Todo.class);
        todo.setUser(user);
        todo.setCreatedBy(loggedInUserId);
        todo.setByAdmin(user.getId() != loggedInUserId);

        Todo createdTodo = todoService.createTodo(todo);

        return createdTodo;
    }

    @PutMapping("/users/{userId}/todos/{todoId}")
    public Todo updateTodo(@RequestBody TodoDto td, @PathVariable Long userId) {

        Todo todo = todoService.getTodo(td.getId());

        todo.setDescription(td.description);
        todo.setDone(td.done);
        todo.setName(td.name);
        todo.setTargetDate(td.targetDate);

        return todoService.updateTodo(todo);
    }

    @DeleteMapping("/users/{userId}/todos/{todoId}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long todoId, @PathVariable Long userId) {
        return todoService.deleteTodo(todoId);
    }
}
