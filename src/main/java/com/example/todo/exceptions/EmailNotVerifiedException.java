package com.example.todo.exceptions;

public class EmailNotVerifiedException extends RuntimeException {

    private static final String message = "Email not verified yet";

    public EmailNotVerifiedException() {
        super(message);
    }

    public EmailNotVerifiedException(String message) {
        super(message);
    }
}