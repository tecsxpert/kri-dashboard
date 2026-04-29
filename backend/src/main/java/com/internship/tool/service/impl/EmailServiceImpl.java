package com.internship.tool.service.impl;

import com.internship.tool.entity.Kri;
import com.internship.tool.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * Email service implementation using JavaMailSender + Thymeleaf templates.
 * All methods run asynchronously to avoid blocking the main thread.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Async
    @Override
    public void sendKriAlertEmail(String to, Kri kri) {
        try {
            Context ctx = new Context();
            ctx.setVariable("kri", kri);
            String htmlContent = templateEngine.process("kri-alert", ctx);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("[KRI Alert] " + kri.getName() + " — Status: " + kri.getStatus());
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("KRI alert email sent to {} for KRI id={}", to, kri.getId());
        } catch (MessagingException e) {
            log.error("Failed to send KRI alert email to {}: {}", to, e.getMessage());
        }
    }

    @Async
    @Override
    public void sendWelcomeEmail(String to, String username) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("Welcome to KRI Dashboard!");
            helper.setText(
                "<h2 style='color:#1e3a5f;'>Welcome, " + username + "!</h2>" +
                "<p>Your KRI Dashboard account has been created successfully.</p>" +
                "<p>You can now log in and start managing Key Risk Indicators.</p>",
                true
            );
            mailSender.send(message);
            log.info("Welcome email sent to {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send welcome email to {}: {}", to, e.getMessage());
        }
    }
}
