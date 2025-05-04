package com.emailtracker.repository;

import com.emailtracker.model.OAuthUserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for OAuth user data.
 * Provides methods to interact with oauth_user_data table.
 */
@Repository
public interface OAuthUserRepository extends JpaRepository<OAuthUserData, String> {
    // No additional methods needed beyond those provided by JpaRepository
}
