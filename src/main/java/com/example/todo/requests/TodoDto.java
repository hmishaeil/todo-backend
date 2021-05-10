package com.example.todo.requests;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

import java.util.Date;

@Data
public class TodoDto {

    public String id;

    @NotEmpty(message = "name is a required field")
    public String name;

    @NotEmpty(message = "description is a required field")
    public String description;

    public boolean done = false;

    public Date targetDate;
}
