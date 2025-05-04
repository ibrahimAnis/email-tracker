package com.emailtracker.repository;

import com.emailtracker.model.EmailTrackingData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for email tracking data.
 * Provides methods to interact with email_tracking_data table.
 */
@Repository
public interface EmailTrackingRepository extends JpaRepository<EmailTrackingData, String> {
    // No additional methods needed beyond those provided by JpaRepository
}
