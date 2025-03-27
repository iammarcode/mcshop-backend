package com.marco.mcshop.auth.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "mcshop.mail")
public class EmailProperties {
    private String host;
    private Integer port;
    private String username; // load from secretsmanager
    private String password; // load from secretsmanager
    private String protocol;
    private String smtpAuth;
    private String smtpStarttlsEnable;
    private String smtpDebug;
}




