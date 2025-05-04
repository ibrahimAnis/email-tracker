package com.emailtracker.controller;

import java.util.*;
import com.emailtracker.service.PixelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Controller for handling tracking pixel requests.
 * Records tracking information and serves a transparent 1x1 pixel.
 */
@Controller
@RequestMapping("/pixel")
@RequiredArgsConstructor
@Slf4j
public class PixelController {

    private final PixelService pixelService;

    /**
     * Endpoint to serve the tracking pixel and record tracking information
     * 
     * @param trackingId The unique tracking ID for the email
     * @param request    The HTTP request object to extract headers and IP
     * @return A transparent 1x1 PNG image
     */
    @GetMapping("/{trackingId}")
    public ResponseEntity<byte[]> trackPixel(@RequestHeader Map<String, String> headerList,
            @PathVariable String trackingId, HttpServletRequest request) {
        log.info("Tracking pixel request received for ID: {}", trackingId);

        headerList.forEach((name, value) -> log.info("Header '{}' = {}", name, value));
        // Extract tracking information
        String userAgent = request.getHeader("User-Agent");

        String ipAddress = getClientIpAddress(request);

        // Record the pixel view
        pixelService.recordPixelView(trackingId, userAgent, ipAddress);

        // Return the 1x1 transparent pixel image
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(pixelService.getTransparentPixel());
    }

    /**
     * Extract client IP address from request, handling proxy scenarios
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("Proxy-Client-IP");
        }
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("WL-Proxy-Client-IP");
        }
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getRemoteAddr();
        }

        // If X-Forwarded-For contains multiple IPs, take the first one
        if (clientIp != null && clientIp.contains(",")) {
            clientIp = clientIp.split(",")[0].trim();
        }

        return clientIp;
    }
}
