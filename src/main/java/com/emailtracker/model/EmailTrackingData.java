package com.emailtracker.model;

import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * Entity class for storing email tracking data.
 * Represents each time a tracking pixel is viewed.
 */
@Entity
@Table(name = "email_tracking_data")
@Data
public class EmailTrackingData {

    @Id
    private String trackingId;

    private String recipientEmail;

    @Lob
    private String userAgent;

    private String ipAddress;

    private String proxyIPAddress;

    private LocalDateTime mailOpenedAt;

    private Boolean isMailForwarded = false;

    private LocalDateTime timestamp;
}
