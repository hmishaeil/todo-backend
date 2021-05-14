package com.example.todo.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource) {
        super(String.format("%s", resource + " not found."));
    }
}