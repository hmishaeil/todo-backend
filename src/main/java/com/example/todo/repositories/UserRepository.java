package com.example.todo.repositories;

import com.example.todo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameIgnoreCase(String email);
    
    // @Query("SELECT u FROM User u INNER JOIN u.confirmationToken c WHERE c.confirmationToken = :ct")
    // public User joinByConfirmationTokens(@Param("ct") String token);
}
