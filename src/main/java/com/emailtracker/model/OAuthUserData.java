package com.emailtracker.model;

import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity class for storing OAuth user data.
 * Represents user information captured from Google OAuth.
 */
@Entity
@Table(name = "oauth_user_data")
@Data
public class OAuthUserData {

    @Id
    private UUID Id;

    private String email;

    private String name;

    private String ipAddress;

    private LocalDateTime timestamp;
}
