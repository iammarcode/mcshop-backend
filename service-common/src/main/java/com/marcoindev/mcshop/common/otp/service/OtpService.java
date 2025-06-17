package com.marcoindev.mcshop.common.otp.service;

public interface OtpService {
    String generateOTP(String key);

    String getOtpByKey(String key);

    void clearOtpByKey(String key);
}
