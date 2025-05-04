package com.emailtracker.dto;

import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object for email sending requests.
 */
@Data
public class EmailRequestDto {

    @NotBlank(message = "Recipient email is required")
    @Email(message = "Invalid email format")
    private String recipient;
    
    @NotBlank(message = "Subject is required")
    private String subject;
}
