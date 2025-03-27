package com.marco.mcshop.auth.service;

public interface OtpService {
    String generateOTP(String key);

    String getOtpByKey(String key);

    void clearOtpByKey(String key);
}
