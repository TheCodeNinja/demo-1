package com.example.demo.service.impl;

import com.example.demo.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender sender;

    public EmailServiceImpl(JavaMailSender sender) {
        this.sender = sender;
    }

    @Async
    @Override
    public void sendEmail(String taskId) throws MessagingException {
        // MIME - HTML message
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        // helper.setTo(emailConfirmationToken.getUser().getUsername());
        helper.setTo("mario@example.com");
        helper.setSubject("Confirm you E-Mail - MFA Application Registration");
        helper.setText("<html>" +
                        "<body>" +
                        "<h2>Dear all,</h2>" +
                        "<br/> The following task is not acked by supporters: " +
                        "<br/> "  + taskId +"" +
                        "<br/> Best Regards,<br/>" +
                        "Call Center Help Desk" +
                        "</body>" +
                        "</html>", true);

        sender.send(message);
    }

}