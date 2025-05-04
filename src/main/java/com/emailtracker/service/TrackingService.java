package com.emailtracker.service;

import com.emailtracker.model.EmailTrackingData;
import com.emailtracker.model.OAuthUserData;
import com.emailtracker.repository.EmailTrackingRepository;
import com.emailtracker.repository.OAuthUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for tracking data operations.
 * Handles retrieval and management of tracking information.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TrackingService {

    private final EmailTrackingRepository emailTrackingRepository;
    private final OAuthUserRepository oAuthUserRepository;

    /**
     * Get all email tracking data
     * @return List of all pixel tracking records
     */
    public List<EmailTrackingData> getAllEmailTrackingData() {
        return emailTrackingRepository.findAll();
    }

    /**
     * Get all OAuth user data
     * @return List of all OAuth user records
     */
    public List<OAuthUserData> getAllOAuthUserData() {
        return oAuthUserRepository.findAll();
    }
    
    /**
     * Extract the current OAuth state (tracking ID) from the authentication context
     * @return The tracking ID or null if not available
     */
    public String getCurrentOAuthState() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            // Extract the state parameter from the authentication details
            // This approach may need adjustment based on the specific OAuth implementation
            return oauthToken.getDetails() != null ? oauthToken.getDetails().toString() : null;
        }
        
        return null;
    }
}
