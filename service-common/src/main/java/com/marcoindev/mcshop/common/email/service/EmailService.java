package com.marcoindev.mcshop.common.email.service;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
}
