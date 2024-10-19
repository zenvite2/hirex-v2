package com.ptit.hirex.service;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    boolean sendMail(String to, String subject, String text);
}
