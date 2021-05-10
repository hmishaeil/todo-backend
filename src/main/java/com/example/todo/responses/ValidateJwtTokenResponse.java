package com.example.todo.responses;

import javax.validation.constraints.NotNull;

public class ValidateJwtTokenResponse {
    
    @NotNull(message = "valid field is required")
    public boolean valid;

}
