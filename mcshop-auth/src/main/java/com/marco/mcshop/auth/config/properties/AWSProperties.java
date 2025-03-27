package com.marco.mcshop.auth.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "mcshop.aws")
public class AWSProperties {
    // topics
    private String customerOtpTopic;
    private String customerRegisterTopic;

    // queues
    private String customerOtpQueue;
    private String customerRegisterQueue;
}
