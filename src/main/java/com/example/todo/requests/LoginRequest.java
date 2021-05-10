package com.example.todo.requests;

import javax.validation.constraints.NotEmpty;

public class LoginRequest {
    @NotEmpty(message = "username is a required field")
    public String username;

    @NotEmpty(message = "password is a required field")
    public String password;
}
