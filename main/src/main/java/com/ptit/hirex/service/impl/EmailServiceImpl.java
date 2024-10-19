package com.ptit.hirex.service.impl;

import com.ptit.hirex.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;
    private final String sender;

    public EmailServiceImpl(JavaMailSender emailSender, @Value("${spring.mail.from}") String sender) {
        this.emailSender = emailSender;
        this.sender = sender;
    }

    public boolean sendMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom(sender);

        try {
            emailSender.send(message);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

}
