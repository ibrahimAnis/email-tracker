package com.emailtracker.service;

import com.emailtracker.model.EmailTrackingData;
import com.emailtracker.repository.EmailTrackingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Service for tracking pixel operations.
 * Handles pixel image serving and tracking data recording.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PixelService {

    private final EmailTrackingRepository trackingRepository;
    private byte[] transparentPixel;

    /**
     * Record a pixel view with tracking data
     * 
     * @param trackingId The unique tracking ID from the email
     * @param userAgent  The user agent string from the request
     * @param ipAddress  The IP address of the requester
     */
    public void recordPixelView(String trackingId, String userAgent, String ipAddress) {
        try {
            Optional<EmailTrackingData> existingData = trackingRepository.findById(trackingId);
            log.info("Recording Pixel Data for ID: {}", trackingId);
            if (existingData.isPresent()) {
                EmailTrackingData trackingData = existingData.get();
                if (trackingData.getMailOpenedAt() == null) {
                    trackingData.setUserAgent(userAgent);
                    trackingData.setProxyIPAddress(ipAddress);
                    trackingData.setTimestamp(LocalDateTime.now());
                    trackingData.setMailOpenedAt(LocalDateTime.now());
                } else if (!trackingData.getUserAgent().equals(userAgent)
                        || !(trackingData.getProxyIPAddress().equals(ipAddress))) {
                    trackingData.setIsMailForwarded(true);
                    trackingData.setProxyIPAddress(ipAddress);
                    trackingData.setTimestamp(LocalDateTime.now());
                }
                trackingRepository.save(trackingData);
                log.info("Recorded pixel view for tracking ID: {}", trackingId);
            }
        } catch (

        Exception e) {
            log.error("Error recording pixel view for tracking ID: {}", trackingId, e);
        }
    }

    /**
     * Get the transparent pixel image
     * 
     * @return byte array containing the 1x1 transparent PNG
     */
    public byte[] getTransparentPixel() {
        if (transparentPixel == null) {
            try {
                ClassPathResource resource = new ClassPathResource("static/images/pixel.png");
                transparentPixel = resource.getInputStream().readAllBytes();
            } catch (IOException e) {
                log.error("Error loading transparent pixel", e);
                // Fallback to a hardcoded transparent 1x1 PNG if file loading fails
                transparentPixel = new byte[] {
                        (byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A, 0x00, 0x00, 0x00, 0x0D,
                        0x49, 0x48, 0x44, 0x52, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x01, 0x08,
                        0x06, 0x00, 0x00, 0x00, 0x1F, 0x15, (byte) 0xC4, (byte) 0x89, 0x00, 0x00, 0x00, 0x0A,
                        0x49, 0x44, 0x41, 0x54, 0x78, (byte) 0x9C, 0x63, 0x00, 0x01, 0x00, 0x00, 0x05, 0x00,
                        0x01, 0x0D, 0x0A, 0x2D, (byte) 0xB4, 0x00, 0x00, 0x00, 0x00, 0x49, 0x45, 0x4E, 0x44,
                        (byte) 0xAE, 0x42, 0x60, (byte) 0x82
                };
            }
        }
        return transparentPixel;
    }
}
