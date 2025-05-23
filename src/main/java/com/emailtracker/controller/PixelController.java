package com.emailtracker.controller;

import java.util.*;
import com.emailtracker.service.PixelService;
import com.emailtracker.util.IpAddressExtractor;

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
    public ResponseEntity<byte[]> trackPixel(@PathVariable String trackingId, HttpServletRequest request) {
        log.info("Tracking pixel request received for ID: {}", trackingId);

        // Extract tracking information
        String userAgent = request.getHeader("User-Agent");

        String ipAddress = IpAddressExtractor.getClientIpAddress(request);

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
}
