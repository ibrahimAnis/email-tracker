package com.emailtracker.controller;

import com.emailtracker.model.EmailTrackingData;
import com.emailtracker.model.OAuthUserData;
import com.emailtracker.repository.EmailTrackingRepository;
import com.emailtracker.repository.OAuthUserRepository;
import com.emailtracker.service.TrackingService;
import com.emailtracker.util.IpAddressExtractor;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Controller for handling OAuth-related endpoints.
 * Processes Google OAuth callbacks and user data capture.
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class OAuthController {

    private final OAuthUserRepository oAuthUserRepository;
    private final TrackingService trackingService;
    private final EmailTrackingRepository trackingRepository;

    /**
     * Endpoint that initiates OAuth flow with a tracking ID
     * The user will be redirected to Google's authentication page
     */
    @GetMapping("/auth/{trackingId}")
    public String initiateOAuth(@PathVariable String trackingId) {
        // Store the tracking ID in session or use a more robust approach
        // for production environments to retrieve it after OAuth callback
        log.info("Initiating OAuth flow for tracking ID: {}", trackingId);
        return "redirect:/oauth2/authorization/google?state=" + trackingId;
    }

    /**
     * OAuth success callback handler
     * Captures user details from Google and stores them with the tracking ID
     */
    @GetMapping("/success")
    public RedirectView oauthSuccess(@AuthenticationPrincipal OAuth2User principal, HttpServletRequest request) {
        String state = trackingService.getCurrentOAuthState();
        String trackingId = (state != null) ? state : "unknown";

        log.info("OAuth completed for tracking ID: {}", trackingId);

        try {
            if (principal != null) {
                String email = principal.getAttribute("email");
                String name = principal.getAttribute("name");

                if (email != null) {
                    OAuthUserData userData = new OAuthUserData();
                    userData.setTrackingId(trackingId);
                    userData.setEmail(email);
                    userData.setName(name);
                    userData.setTimestamp(LocalDateTime.now());
                    userData.setIpAddress(IpAddressExtractor.getClientIpAddress(request));
                    oAuthUserRepository.save(userData);
                    log.info("Saved OAuth user data for email: {}", email);

                    Optional<EmailTrackingData> existingData = trackingRepository.findById(trackingId);
                    if (existingData.isPresent()) {
                        EmailTrackingData trackingData = existingData.get();
                        trackingData.setIpAddress(userData.getIpAddress());
                        trackingData.setTimestamp(LocalDateTime.now());
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error processing OAuth data", e);
        }

        // Redirect to success page
        return new RedirectView("/success.html");
    }
}
