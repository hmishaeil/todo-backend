package com.example.todo.exceptions;

public class LoginAttemptsExceededException extends RuntimeException {

    private static String message = "Your account is being locked. Try again in 15 minutes.";

    public LoginAttemptsExceededException() {
        super(message);
    }

}
