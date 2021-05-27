package com.example.todo.requests;

import lombok.Data;

@Data
public class UpdateUserRequest {

    private String username;
    private Boolean enabled;
    private String internalNote;

}
