package com.example.todo.requests;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class SignUpRequest {

    @NotEmpty(message = "email is a required field")
    private String email;
    
    @NotEmpty(message = "username is a required field")
    private String username;

    @NotEmpty(message = "password is a required field")
    private String password;

}
