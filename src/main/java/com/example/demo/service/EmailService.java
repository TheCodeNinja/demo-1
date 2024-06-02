package com.example.demo.service;

import jakarta.mail.MessagingException;
import org.springframework.scheduling.annotation.Async;

public interface EmailService {

    @Async
    void sendEmail(String taskId) throws MessagingException;
}
