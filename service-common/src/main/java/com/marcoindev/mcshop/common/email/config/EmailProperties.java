package com.marcoindev.mcshop.common.email.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "chatbot.mail")
public class EmailProperties {
    private String host;
    private Integer port;
    private String username;
    private String password;
    private String protocol;
    private String smtpAuth;
    private String smtpStarttlsEnable;
    private String smtpDebug;
}




