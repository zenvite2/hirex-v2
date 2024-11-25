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
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Slf4j
@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final String sender;
    private final TemplateEngine templateEngine;

    public MailServiceImpl(JavaMailSender mailSender, @Value("${spring.mail.from}") String sender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.sender = sender;
        this.templateEngine = templateEngine;
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

    @Override
    public void sendJobApplicationEmail(String to, String subject, Map<String, Object> templateModel) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        Context context = new Context();
        context.setVariables(templateModel);
        String htmlContent = templateEngine.process("notification-email", context);

        helper.setText(htmlContent, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setFrom(sender);

        mailSender.send(message);
    }

    @Override
    public void sendOTPEmail(String toEmail, String otp) throws MessagingException {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(sender);
            helper.setTo(toEmail);
            helper.setSubject("Your OTP Code");

            // Load HTML template
            String htmlContent = "<!DOCTYPE html>" +
                    "<html lang='en'>" +
                    "<head>" +
                    "<link rel='preconnect' href='https://fonts.googleapis.com'>" +
                    "<link rel='preconnect' href='https://fonts.gstatic.com' crossorigin>" +
                    "<link href='https://fonts.googleapis.com/css2?family=Noto+Sans:wght@100..900&display=swap' rel='stylesheet'>" +
                    "<style>" +
                    "/* CSS styles here */" +
                    "html, body { background-color: #e4e4e4; font-family: 'Noto Sans', sans-serif !important; }" +
                    ".container { margin: auto; padding: 1rem; background-color: white; border-radius: 1rem; max-width: 500px; border-top: 5px solid #002868; }" +
                    ".otp-section-cell { font-size: 3rem; background-color: #DBDBDB; padding: 10px; text-align: center; letter-spacing: 0.7rem; }" +
                    ".otp-intro { text-align: center; font-size: 1.5rem; font-weight: bold; margin-bottom: 1rem; }" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class='container'>" +
                    "    <h2>Your OTP Code</h2>" +
                    "    <p class='otp-intro'>Here is your OTP code:</p>" +
                    "    <div class='otp-section-cell'>" + otp + "</div>" +
                    "    <p>This code will expire in 5 minutes. Please do not share it with anyone.</p>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            // Set the content of the email
            helper.setText(htmlContent, true);

            // Send email
            mailSender.send(message);
            log.info("OTP email sent successfully to: {}", toEmail);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
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
