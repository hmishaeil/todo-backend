package com.example.todo.repositories;

import com.example.todo.entities.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query(value = "select * from todos t where t.user_id = :user_id", nativeQuery = true)
    List<Todo> getUserTodos(@Param("user_id") Long userId);
}
