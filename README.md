# Email Tracking Pixel System with OAuth2 Integration

A Proof of Concept (PoC) application using Spring Boot (Java) to implement an email tracking pixel system with Google OAuth2 integration.

## Features

- Email sending with embedded tracking pixel and OAuth link
- Tracking pixel implementation to record email opens
- Google OAuth2 integration to capture user details
- Data storage for tracking information
- REST API for sending emails and retrieving tracking data

## Prerequisites

- Java 17 or later
- Maven
- Gmail account with App Password configured (for SMTP)
- Google Developer Account with OAuth2 credentials

## Configuration

### 1. Gmail SMTP Configuration

To send emails, you need to configure Gmail SMTP in `application.properties`:

```properties
spring.mail.username=your.email@gmail.com
spring.mail.password=your-app-password
