package com.example.todo.services.interfaces;

public interface IEmailService {

        public void sendEmail(String emailType, String email, String token);
}