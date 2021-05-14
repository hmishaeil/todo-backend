package com.example.todo.exceptions;

public class SendingEmailException extends RuntimeException {
    
    public SendingEmailException(String message) {
        super(message);
    }
}
