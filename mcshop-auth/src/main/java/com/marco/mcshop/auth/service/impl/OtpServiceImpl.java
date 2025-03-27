package com.marco.mcshop.auth.service.impl;

import com.marco.mcshop.auth.service.OtpService;
import com.marco.mcshop.auth.service.RedisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OtpServiceImpl implements OtpService {
    private final RedisService<String, Object> redisService;

    public OtpServiceImpl(RedisService<String, Object> redisService) {
        this.redisService = redisService;
    }

    @Value("${mcshop.otp.expire.second}")
    private Long OTP_EXPIRE_SECOND;

    @Override
    public String generateOTP(String key) {
        Random random = new Random();
        String OTP = String.valueOf(100000 + random.nextInt(900000));

        redisService.set(key, OTP, OTP_EXPIRE_SECOND);

        return OTP;
    }

    @Override
    public String getOtpByKey(String key) {
        Object otp = redisService.get(key);

        return (String) otp;
    }

    @Override
    public void clearOtpByKey(String key) {
        redisService.del(key);
    }


}
