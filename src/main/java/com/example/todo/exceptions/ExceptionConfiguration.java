package com.example.todo.exceptions;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExceptionConfiguration {
    
    @Bean
    public ExceptionResponse exceptionResponse(){
        return new ExceptionResponse();
    }
}
