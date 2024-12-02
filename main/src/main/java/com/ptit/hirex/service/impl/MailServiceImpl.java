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

    @Override
    public void sendEmailFollow(String toEmail, String companyName, String jobPostUrl) throws MessagingException {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(sender);
            helper.setTo(toEmail);
            helper.setSubject("New Job Alert");

            // Load HTML template
            String htmlContent = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <link rel=\"preconnect\" href=\"https://fonts.googleapis.com\">\n" +
                    "    <link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin>\n" +
                    "    <link href=\"https://fonts.googleapis.com/css2?family=Noto+Sans:wght@400;700&display=swap\" rel=\"stylesheet\">\n" +
                    "    <title>New Job Alert</title>\n" +
                    "    <style>\n" +
                    "        body {\n" +
                    "            background-color: #f4f4f4;\n" +
                    "            font-family: 'Noto Sans', sans-serif;\n" +
                    "            margin: 0;\n" +
                    "            padding: 0;\n" +
                    "        }\n" +
                    "        .container {\n" +
                    "            margin: 2rem auto;\n" +
                    "            padding: 1.5rem;\n" +
                    "            max-width: 600px;\n" +
                    "            background-color: #ffffff;\n" +
                    "            border-radius: 8px;\n" +
                    "            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);\n" +
                    "            border-top: 5px solid #007bff;\n" +
                    "        }\n" +
                    "        h1 {\n" +
                    "            color: #333333;\n" +
                    "            font-size: 1.8rem;\n" +
                    "            margin-bottom: 0.5rem;\n" +
                    "            text-align: center;\n" +
                    "        }\n" +
                    "        p {\n" +
                    "            color: #555555;\n" +
                    "            font-size: 1rem;\n" +
                    "            line-height: 1.5;\n" +
                    "            text-align: center;\n" +
                    "        }\n" +
                    "        .job-link {\n" +
                    "            display: block;\n" +
                    "            margin: 1.5rem auto;\n" +
                    "            padding: 0.75rem 1.5rem;\n" +
                    "            text-align: center;\n" +
                    "            background-color: #007bff;\n" +
                    "            color: #ffffff;\n" +
                    "            font-size: 1.2rem;\n" +
                    "            font-weight: bold;\n" +
                    "            text-decoration: none;\n" +
                    "            border-radius: 5px;\n" +
                    "            box-shadow: 0 4px 6px rgba(0, 123, 255, 0.2);\n" +
                    "            transition: background-color 0.3s ease;\n" +
                    "        }\n" +
                    "        .job-link:hover {\n" +
                    "            background-color: #0056b3;\n" +
                    "        }\n" +
                    "        footer {\n" +
                    "            text-align: center;\n" +
                    "            margin-top: 2rem;\n" +
                    "            font-size: 0.9rem;\n" +
                    "            color: #999999;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <div class=\"container\">\n" +
                    "        <h1>New Job Alert!</h1>\n" +
                    "        <p>The company <strong>{{companyName}}</strong> you are following just posted a new job on HIrex!</p>\n" +
                    "        <p>Click the button below to check it out now:</p>\n" +
                    "        <a href=\"{{jobPostUrl}}\" class=\"job-link\">View Job</a>\n" +
                    "    </div>\n" +
                    "    <footer>\n" +
                    "        <p>Thank you for using HIrex!</p>\n" +
                    "    </footer>\n" +
                    "</body>\n" +
                    "</html>\n";

            // Replace placeholders with actual values
            htmlContent = htmlContent.replace("{{companyName}}", companyName);
            htmlContent = htmlContent.replace("{{jobPostUrl}}", jobPostUrl);

            // Set the content of the email
            helper.setText(htmlContent, true);

            // Send email
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    @Override
    public void sendEmailPost(String toEmail, String username, String messageJob, String websiteUrl) throws MessagingException {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(sender);
            helper.setTo(toEmail);
            helper.setSubject("New Job Alert");

            // Load HTML template
            String htmlContent = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <title>Notification Email</title>\n" +
                    "    <style>\n" +
                    "        body {\n" +
                    "            font-family: Arial, sans-serif;\n" +
                    "            line-height: 1.6;\n" +
                    "            background-color: #f9f9f9;\n" +
                    "            margin: 0;\n" +
                    "            padding: 0;\n" +
                    "        }\n" +
                    "        .email-container {\n" +
                    "            max-width: 600px;\n" +
                    "            margin: 20px auto;\n" +
                    "            background: #fff;\n" +
                    "            padding: 20px;\n" +
                    "            border-radius: 10px;\n" +
                    "            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);\n" +
                    "        }\n" +
                    "        .header {\n" +
                    "            background-color: gray;\n" +
                    "            color: white;\n" +
                    "            padding: 10px 20px;\n" +
                    "            text-align: center;\n" +
                    "            border-radius: 10px 10px 0 0;\n" +
                    "        }\n" +
                    "        .content {\n" +
                    "            padding: 20px;\n" +
                    "        }\n" +
                    "        .footer {\n" +
                    "            text-align: center;\n" +
                    "            margin-top: 20px;\n" +
                    "            font-size: 12px;\n" +
                    "            color: #888;\n" +
                    "        }\n" +
                    "        .btn {\n" +
                    "            display: inline-block;\n" +
                    "            padding: 10px 15px;\n" +
                    "            background-color: gray;\n" +
                    "            color: white;\n" +
                    "            text-decoration: none;\n" +
                    "            border-radius: 5px;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <div class=\"email-container\">\n" +
                    "        <div class=\"header\">\n" +
                    "            <h1>Job Notification</h1>\n" +
                    "        </div>\n" +
                    "        <div class=\"content\">\n" +
                    "            <p>Xin chào <strong>{{username}}</strong>,</p>\n" +
                    "            <p>{{message}}</p>\n" +
                    "            <p>Nếu có bất kỳ câu hỏi nào, vui lòng liên hệ với chúng tôi qua email hoặc đường dây nóng.</p>\n" +
                    "            <a href=\"{{websiteUrl}}\" class=\"btn\">Truy cập Website</a>\n" +
                    "        </div>\n" +
                    "        <div class=\"footer\">\n" +
                    "            <p>&copy; 2024 Your Job Website. All Rights Reserved.</p>\n" +
                    "        </div>\n" +
                    "    </div>\n" +
                    "</body>\n" +
                    "</html>\n";

            // Replace placeholders with actual values
            htmlContent = htmlContent.replace("{{username}}", username);
            htmlContent = htmlContent.replace("{{message}}", messageJob);
            htmlContent = htmlContent.replace("{{websiteUrl}}", websiteUrl);

            // Set the content of the email
            helper.setText(htmlContent, true);

            // Send email
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    @Override
    public void sendEmailCMS(String subject, String toEmail, String username, String messageCms, String websiteUrl) throws MessagingException {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(sender);
            helper.setTo(toEmail);
            helper.setSubject(subject);

            // Load HTML template
            String htmlContent = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <title>Notification Email</title>\n" +
                    "    <style>\n" +
                    "        body {\n" +
                    "            font-family: Arial, sans-serif;\n" +
                    "            line-height: 1.6;\n" +
                    "            background-color: #f9f9f9;\n" +
                    "            margin: 0;\n" +
                    "            padding: 0;\n" +
                    "        }\n" +
                    "        .email-container {\n" +
                    "            max-width: 600px;\n" +
                    "            margin: 20px auto;\n" +
                    "            background: #fff;\n" +
                    "            padding: 20px;\n" +
                    "            border-radius: 10px;\n" +
                    "            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);\n" +
                    "        }\n" +
                    "        .header {\n" +
                    "            background-color: gray;\n" +
                    "            color: white;\n" +
                    "            padding: 10px 20px;\n" +
                    "            text-align: center;\n" +
                    "            border-radius: 10px 10px 0 0;\n" +
                    "        }\n" +
                    "        .content {\n" +
                    "            padding: 20px;\n" +
                    "        }\n" +
                    "        .footer {\n" +
                    "            text-align: center;\n" +
                    "            margin-top: 20px;\n" +
                    "            font-size: 12px;\n" +
                    "            color: #888;\n" +
                    "        }\n" +
                    "        .btn {\n" +
                    "            display: inline-block;\n" +
                    "            padding: 10px 15px;\n" +
                    "            background-color: gray;\n" +
                    "            color: white;\n" +
                    "            text-decoration: none;\n" +
                    "            border-radius: 5px;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <div class=\"email-container\">\n" +
                    "        <div class=\"header\">\n" +
                    "            <h1>HireX Notification</h1>\n" +
                    "        </div>\n" +
                    "        <div class=\"content\">\n" +
                    "            <p>Xin chào <strong>{{username}}</strong>,</p>\n" +
                    "            <p>{{message}}</p>\n" +
                    "            <p>Nếu có bất kỳ câu hỏi nào, vui lòng liên hệ với chúng tôi qua email hoặc đường dây nóng.</p>\n" +
                    "            <a href=\"{{websiteUrl}}\" class=\"btn\">Truy cập Website</a>\n" +
                    "        </div>\n" +
                    "        <div class=\"footer\">\n" +
                    "            <p>&copy; 2024 Your Job Website. All Rights Reserved.</p>\n" +
                    "        </div>\n" +
                    "    </div>\n" +
                    "</body>\n" +
                    "</html>\n";

            // Replace placeholders with actual values
            htmlContent = htmlContent.replace("{{username}}", username);
            htmlContent = htmlContent.replace("{{message}}", messageCms);
            htmlContent = htmlContent.replace("{{websiteUrl}}", websiteUrl);

            // Set the content of the email
            helper.setText(htmlContent, true);

            // Send email
            mailSender.send(message);
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
