package com.example.todo.responses;

import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Setter
public class SignUpResponse {
    
    @NotEmpty(message = "token is a required field")
    private String token;
}
