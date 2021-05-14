package com.example.todo.responses;

import lombok.Data;

import java.io.Serializable;

@Data
public class JwtTokenResponse implements Serializable {

    private static final long serialVersionUID = 8317676219297719109L;

    private final String token;

}