package com.ptit.hirex.service;

import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    boolean sendMail(String to, String subject, String text);

    boolean sendHtmlEmail(String to, String subject, String htmlBody) throws MessagingException;
}
