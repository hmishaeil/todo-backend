package com.example.todo.services.interfaces;

import com.example.todo.entities.Todo;
import com.example.todo.exceptions.ResourceNotFoundException;
import com.example.todo.repositories.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IToDoService {

    @Autowired
    TodoRepository todoRepository;

    public List<Todo> getTodosForUser(Long userId) {
        return todoRepository.getUserTodos(userId);
    }

    public List<Todo> getTodos() {
        return todoRepository.findAll();
    }

    public Todo createTodo(Todo td) {
        return todoRepository.save(td);
    }

    public Todo updateTodo(Todo td) {
        return todoRepository.save(td);
    }

    public Todo getTodo(Long id) {

        Optional<Todo> todo = todoRepository.findById(id);

        if (todo.isEmpty()) {
            throw new ResourceNotFoundException(String.format("%s", "Todo not found"));
        }

        return todo.get();
    }

    public ResponseEntity<Void> deleteTodo(Long id) {

        Optional<Todo> todo = todoRepository.findById(id);

        if (todo.isEmpty()) {
            throw new ResourceNotFoundException(String.format("%s", "Todo not found"));
        }

        todoRepository.delete(todo.get());
        return ResponseEntity.noContent().build();
    }

    public List<Todo> getUserTodos(Long userId){
        return todoRepository.getUserTodos(userId);
    }
}
