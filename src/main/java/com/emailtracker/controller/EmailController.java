package com.emailtracker.controller;

import com.emailtracker.dto.EmailRequestDto;
import com.emailtracker.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.concurrent.CompletableFuture;

/**
 * REST controller for email-related operations.
 * Handles email sending requests.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class EmailController {

    private final EmailService emailService;

    /**
     * Endpoint to send emails with tracking pixel and OAuth link
     * 
     * @param emailRequest The email request containing recipient information
     * @return Response with success/failure message
     */
    @PostMapping("/send-email")
    public CompletableFuture<ResponseEntity<String>> sendEmail(@Valid @RequestBody EmailRequestDto emailRequest)
            throws MessagingException {
        log.info("Received request to send email to: {}", emailRequest.getRecipient());
        return emailService.sendEmailWithTracking(emailRequest.getRecipient(), emailRequest.getSubject())
                .thenApply(trackingId -> ResponseEntity.ok("Email sent with tracking ID: " + trackingId))
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to send email: " + ex.getMessage()));
    }
}
