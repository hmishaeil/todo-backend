package com.example.todo.requests;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class InitResetPasswordRequest {

    @NotNull(message = "username is required.")
    public String username;
}
