package com.ptit.hirex.service.impl;

import com.ptit.hirex.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final String sender;

    public MailServiceImpl(JavaMailSender mailSender, @Value("${spring.mail.from}") String sender) {
        this.mailSender = mailSender;
        this.sender = sender;
    }

    @Override
    public boolean sendMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        try {
            mailSender.send(message);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean sendHtmlEmail(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setFrom(sender);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);

        try {
            mailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public void sendPasswordResetEmail(String to, String newPassword) throws Exception {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(sender);
            helper.setTo(to);
            helper.setSubject("Reset Password Request");

            String emailContent = buildPasswordResetEmailTemplate(newPassword);
            helper.setText(emailContent, true);

            mailSender.send(message);
            log.info("Password reset email sent successfully to: {}", to);

        } catch (Exception e) {
            log.error("Failed to send password reset email to: {}", to, e);
            throw new Exception("Failed to send password reset email", e);
        }
    }

    private String buildPasswordResetEmailTemplate(String newPassword) {
        return """
            <html>
                <body style='margin: 0; padding: 20px; font-family: Arial, sans-serif;'>
                    <div style='background-color: #f5f5f5; padding: 20px; border-radius: 5px;'>
                        <h2 style='color: #333;'>Password Reset</h2>
                        <p>Your password has been reset successfully. Here is your new password:</p>
                        <div style='background-color: #fff; padding: 10px; border-radius: 3px; margin: 10px 0;'>
                            <strong>%s</strong>
                        </div>
                        <p>Please change this password after logging in for security purposes.</p>
                        <p>If you didn't request this password reset, please contact our support team immediately.</p>
                        <br>
                        <p>Best regards,<br>Your Application Team</p>
                    </div>
                </body>
            </html>
            """.formatted(newPassword);
    }
}
