package com.marco.mcshop.auth.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "mcshop.jwt")
public class JwtProperties {
    private String secretKey;
    private Long accessTokenExpiration;
    private Long refreshTokenExpiration;
}
