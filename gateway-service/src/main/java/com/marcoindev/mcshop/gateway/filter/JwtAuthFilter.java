package com.marcoindev.mcshop.gateway.filter;

import com.marcoindev.mcshop.common.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Slf4j
@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;

    @Autowired
    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Jwt filter start");

        // Skip JWT validation for whitelisted paths
        String path = exchange.getRequest().getURI().getPath();
        if (isWhitelisted(path)) {
            log.info("Whitelisted path, skipping JWT validation: {}", path);
            return chain.filter(exchange);
        }

        // Validate JWT
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Missing or invalid Authorization header");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        if (!jwtUtil.isTokenValid(token)) {
            log.warn("Invalid or expired JWT token");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String userId = jwtUtil.getSubject(token);
        log.info("JWT validated, adding X-User-ID: {}", userId);

        // Add X-User-ID header and proceed
        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(r -> r.header("X-User-ID", userId))
                .build();

        log.info("Jwt filter end");
        return chain.filter(mutatedExchange);
    }

    private boolean isWhitelisted(String path) {
        String[] whiteList = {
                "/api/v1/auth/**",
                "/api/v1/product/**",
                "/api-docs/**",
                "/swagger-ui/**"
        };
        return Arrays.stream(whiteList)
                .anyMatch(pattern -> path.matches(pattern.replace("**", ".*")));
    }

    @Override
    public int getOrder() {
        return -1; // Run early in the filter chain
    }
}