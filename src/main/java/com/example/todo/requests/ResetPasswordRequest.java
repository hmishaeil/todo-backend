package com.example.todo.requests;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ResetPasswordRequest {

    @NotNull(message = "password is required.")
    private String password;

    @NotNull(message = "token is required.")
    private String token;
}
