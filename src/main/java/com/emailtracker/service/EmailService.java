package com.emailtracker.service;

import com.emailtracker.model.EmailTrackingData;
import com.emailtracker.repository.EmailTrackingRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Service for email-related operations.
 * Handles email composition and sending.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final EmailTrackingRepository emailTrackingRepository;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    @Value("${app.base-url}")
    private String baseUrl;

    /**
     * Send an email with tracking pixel and OAuth link
     * @param to Recipient email address
     * @param subject Email subject
     * @return The tracking ID for this email
     */
    @Async
    public CompletableFuture<String> sendEmailWithTracking(String to, String subject) throws MessagingException {
        // Generate unique tracking ID
        String trackingId = UUID.randomUUID().toString();
        log.info("Generating tracking ID for email: {}", trackingId);
        
        // Create pixel and OAuth URLs
        String pixelUrl = baseUrl + "/pixel/" + trackingId;
        String oauthUrl = baseUrl + "/auth/" + trackingId;
        
        // Store initial tracking data
        EmailTrackingData trackingData = new EmailTrackingData();
        trackingData.setTrackingId(trackingId);
        trackingData.setRecipientEmail(to);
        trackingData.setTimestamp(LocalDateTime.now());
        emailTrackingRepository.save(trackingData);
        
        // Prepare email template context
        Context context = new Context();
        context.setVariable("pixelUrl", pixelUrl);
        context.setVariable("oauthUrl", oauthUrl);
        context.setVariable("recipientEmail", to);
        
        // Process the template
        String emailContent = templateEngine.process("email-template", context);
        
        // Create the email message
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        
        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(emailContent, true); // true indicates HTML content
        
        // Send the email
        mailSender.send(message);
        log.info("Email sent to {} with tracking ID {}", to, trackingId);
        
        return CompletableFuture.completedFuture(trackingId);
    }
}
