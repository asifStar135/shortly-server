package com.shortly.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EmailEngine {
    @Autowired
    private JavaMailSender mailSender;
//    @Value("${app.mail.from-address}")
//    private String toEmail;

    @Async
    public void sendEmail(String code, String toEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your Password Reset Code");
        message.setText("Your one-time password reset code is: " + code);
        mailSender.send(message);
    }
}
