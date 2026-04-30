package com.internship.tool.service;

import com.internship.tool.entity.KriRecord;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${app.mail.from}")
    private String fromAddress;

    @Value("${app.mail.to-admin}")
    private String toAdmin;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    // ------------------------------------------------------------------ //
    //  Public API                                                          //
    // ------------------------------------------------------------------ //

    /**
     * Sends an HTML notification when a new KRI record is created.
     */
    public void sendCreateNotification(KriRecord record) {
        Context ctx = buildContext(record);
        String html = templateEngine.process("create-notification", ctx);
        sendHtmlEmail(
                toAdmin,
                "New KRI Record Created: " + record.getTitle(),
                html
        );
    }

    /**
     * Sends an HTML notification when a KRI record is overdue.
     * Caller is responsible for checking the overdue condition before invoking.
     */
    public void sendOverdueNotification(KriRecord record) {
        Context ctx = buildContext(record);
        String html = templateEngine.process("overdue-notification", ctx);
        sendHtmlEmail(
                toAdmin,
                "Overdue KRI Record: " + record.getTitle(),
                html
        );
    }

    // ------------------------------------------------------------------ //
    //  Private helpers                                                     //
    // ------------------------------------------------------------------ //

    private Context buildContext(KriRecord record) {
        Context ctx = new Context();
        ctx.setVariable("title",    record.getTitle());
        ctx.setVariable("category", record.getCategory());
        ctx.setVariable("status",   record.getStatus());
        ctx.setVariable("dueDate",  record.getDueDate());
        ctx.setVariable("score",    record.getScore());
        return ctx;
    }

    private void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromAddress);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            mailSender.send(message);
            log.info("Email sent to {} | subject: {}", to, subject);
        } catch (MessagingException ex) {
            log.error("Failed to send email to {} | subject: {} | reason: {}", to, subject, ex.getMessage());
        }
    }
}
