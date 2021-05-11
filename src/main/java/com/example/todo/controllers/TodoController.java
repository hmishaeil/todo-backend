package com.example.todo.controllers;

import com.example.todo.entities.Todo;
import com.example.todo.requests.TodoDto;
import com.example.todo.services.ToDoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:4200")
public class TodoController {

    @Autowired
    ToDoService todoService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/todos")
    public ResponseEntity<List<Todo>> getTodos() {
        return ResponseEntity.ok(todoService.getTodos());
    }

    @GetMapping("/todos/{id}")
    public ResponseEntity<Todo> getTodo(@PathVariable Long id) {
        return ResponseEntity.ok(todoService.getTodo(id));
    }

    @PostMapping("/todos")
    public Todo createTodo(@RequestBody TodoDto td) {

        Todo todo = modelMapper.map(td, Todo.class);
        return todoService.createTodo(todo);
    }

    @PutMapping("/todos")
    public Todo updateTodo(@RequestBody TodoDto td) {
        Todo todo = modelMapper.map(td, Todo.class);
        return todoService.updateTodo(todo);
    }

    @DeleteMapping("/todos/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        return todoService.deleteTodo(id);
    }
}
