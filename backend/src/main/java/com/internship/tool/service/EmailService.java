package com.internship.tool.service;

import com.internship.tool.entity.Kri;

/**
 * Service interface for sending email notifications.
 */
public interface EmailService {

    void sendKriAlertEmail(String to, Kri kri);

    void sendWelcomeEmail(String to, String username);
}
