package com.emailtracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for the application.
 * Configures OAuth2 authentication and endpoint access rules.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        // Public endpoints
                        .requestMatchers("/api/send-email", "/pixel/**", "/auth/**",
                                "/api/tracking", "/success", "/error", "/h2-console/**")
                        .permitAll()
                        .anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/oauth2/authorization/google")
                        .defaultSuccessUrl("/success", true)
                        .failureUrl("/error"))
                .headers(headers -> headers
                        // this HeaderWriter runs on every response
                        .addHeaderWriter((request, response) -> {
                            // grab it from the ?state= query-param
                            String trackingId = request.getParameter(OAuth2ParameterNames.STATE);
                            if (trackingId != null && !trackingId.isBlank()) {
                                response.setHeader("X-Tracking-Id", trackingId);
                            }
                        }));

        return http.build();
    }
}
