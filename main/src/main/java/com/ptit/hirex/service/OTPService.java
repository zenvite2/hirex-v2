package com.ptit.hirex.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class OTPService {
    private final RedisTemplate<String, String> redisTemplate;
    private static final long OTP_VALIDITY_MINUTES = 5L;

    public OTPService(@Qualifier("redisOTPTemplate") RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String generateOTP(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        redisTemplate.opsForValue().set(
                getRedisKey(email),
                otp,
                OTP_VALIDITY_MINUTES,
                TimeUnit.MINUTES
        );
        return otp;
    }

    public boolean validateOTP(String email, String otp) {
        String storedOTP = redisTemplate.opsForValue().get(getRedisKey(email));
        if (storedOTP != null && storedOTP.equals(otp)) {
            redisTemplate.delete(getRedisKey(email)); // Delete OTP after successful validation
            return true;
        }
        return false;
    }

    private String getRedisKey(String email) {
        return "OTP:" + email;
    }
}
