package com.emailtracker.controller;

import com.emailtracker.model.EmailTrackingData;
import com.emailtracker.model.OAuthUserData;
import com.emailtracker.service.TrackingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST controller for retrieving tracking data.
 * Provides endpoints to access collected data for analysis.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class TrackingController {

    private final TrackingService trackingService;

    /**
     * Endpoint to retrieve all tracking data (pixel views and OAuth data)
     * @return Combined tracking information
     */
    @GetMapping("/tracking")
    public ResponseEntity<Map<String, Object>> getAllTrackingData() {
        log.info("Retrieving all tracking data");
        
        List<EmailTrackingData> pixelTrackingData = trackingService.getAllEmailTrackingData();
        List<OAuthUserData> oauthUserData = trackingService.getAllOAuthUserData();
        
        Map<String, Object> response = new HashMap<>();
        response.put("pixelTrackingData", pixelTrackingData);
        response.put("oauthUserData", oauthUserData);
        
        return ResponseEntity.ok(response);
    }
}
