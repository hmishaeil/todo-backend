package com.example.todo.requests;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginRequest {
    @NotNull(message = "username is a required field")
    private String username;

    @NotNull(message = "password is a required field")
    private String password;
}
