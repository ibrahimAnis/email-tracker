package com.emailtracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main application class for the Email Tracker PoC.
 * This application demonstrates email tracking using pixels and OAuth2 integration.
 */
@SpringBootApplication
@EnableAsync // Enable async processing for email sending
public class EmailTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmailTrackerApplication.class, args);
    }
}
