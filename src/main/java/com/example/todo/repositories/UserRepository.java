package com.example.todo.repositories;

import com.example.todo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameIgnoreCase(String email);
    User findByEmailIgnoreCase(String email);
    
    // @Query("SELECT u FROM User u INNER JOIN u.confirmationToken c WHERE c.confirmationToken = :ct")
    // public User joinByConfirmationTokens(@Param("ct") String token);
}
