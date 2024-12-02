package com.ptit.hirex.service;

import jakarta.mail.MessagingException;

import java.util.Map;

public interface MailService {
    boolean sendMail(String to, String subject, String text);

    boolean sendHtmlEmail(String to, String subject, String htmlBody) throws MessagingException;

    void sendPasswordResetEmail(String to, String newPassword) throws Exception;

    void sendJobApplicationEmail(String to, String subject, Map<String, Object> templateModel) throws MessagingException;

    void sendOTPEmail(String toEmail, String otp) throws MessagingException;

    void sendEmailFollow(String toEmail, String companyName, String jobPostUrl) throws MessagingException;

    void sendEmailPost(String toEmail,String username, String messageJob, String websiteUrl) throws MessagingException;

    void sendEmailCMS(String subject, String toEmail, String username, String messageCms, String websiteUrl) throws MessagingException;

}
