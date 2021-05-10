package com.example.todo.requests;

import javax.validation.constraints.NotNull;

public class ValidateJwtTokenRequest {
    
    @NotNull(message = "username is required")
    public String username;
    @NotNull(message = "token is required")
    public String token;
}
