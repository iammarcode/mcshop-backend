package com.marco.mcshop.auth.config;

import com.marco.mcshop.auth.config.properties.EmailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {
    private final EmailProperties emailProperties;

    public EmailConfig(EmailProperties emailProperties) {
        this.emailProperties = emailProperties;
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(emailProperties.getHost());
        mailSender.setPort(emailProperties.getPort());

        mailSender.setUsername(emailProperties.getUsername());
        mailSender.setPassword(emailProperties.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", emailProperties.getProtocol());
        props.put("mail.smtp.auth", emailProperties.getSmtpAuth());
        props.put("mail.smtp.starttls.enable", emailProperties.getSmtpStarttlsEnable());
        props.put("mail.debug", emailProperties.getSmtpDebug());

        return mailSender;
    }
}
