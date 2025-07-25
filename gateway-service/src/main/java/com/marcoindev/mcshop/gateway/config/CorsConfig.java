package com.marcoindev.mcshop.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        
        // Allow requests from your web application
        corsConfig.setAllowedOriginPatterns(List.of(
            "http://localhost:3000",     // Development
            "http://localhost:3001",     // Alternative dev port
            "http://127.0.0.1:3000",     // Alternative localhost
            "http://127.0.0.1:3001"      // Alternative localhost alternative port
        ));
        
        // Allow all HTTP methods
        corsConfig.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));
        
        // Allow all headers
        corsConfig.setAllowedHeaders(Arrays.asList(
            "Origin", "Content-Type", "Accept", "Authorization", 
            "X-Requested-With", "X-User-Id", "Cache-Control"
        ));
        
        // Allow credentials (cookies, authorization headers)
        corsConfig.setAllowCredentials(true);
        
        // Set max age for preflight requests
        corsConfig.setMaxAge(3600L);
        
        // Expose custom headers
        corsConfig.setExposedHeaders(Arrays.asList(
            "X-User-Id", "Authorization"
        ));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        
        return new CorsWebFilter(source);
    }
} 