package com.example.todo.exceptions;

public class EmailNotVerifiedException extends RuntimeException {

    public EmailNotVerifiedException(String message) {
        super(message);
    }
}