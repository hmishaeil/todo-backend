package com.example.todo.requests;

import com.example.todo.entities.Role;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class AddUserRequest {
    
    @NotEmpty(message = "username is a required field")
    private String username;

    @NotEmpty(message = "roles is a required field")
    private String role;

    private Boolean enabled;
    private String internalNote;


}
