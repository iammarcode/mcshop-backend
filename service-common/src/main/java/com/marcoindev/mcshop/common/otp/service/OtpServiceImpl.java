package com.marcoindev.mcshop.common.otp.service;

import com.marcoindev.mcshop.common.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OtpServiceImpl implements OtpService {
    private final RedisService<String, Object> redisService;
    @Value("${mcshop.otp.expire.second}")
    private Long OTP_EXPIRE_SECOND;

    public OtpServiceImpl(RedisService<String, Object> redisService) {
        this.redisService = redisService;
    }

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
