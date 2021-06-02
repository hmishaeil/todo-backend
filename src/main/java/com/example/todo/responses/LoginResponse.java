package com.example.todo.responses;

import lombok.Data;
@Data
public class LoginResponse {

    private String username;
    private String token;
    private String role;
    private Long userId;

}
