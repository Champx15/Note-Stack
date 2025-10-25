package com.champ.notes_app.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    public void saveOtp(String email, String otp) {
        redisTemplate.opsForValue().set("otp:" + email, otp, 300, TimeUnit.SECONDS);
    }

    public String getOtp(String email) {
        return redisTemplate.opsForValue().get("otp:" + email);
    }

    public void deleteOtp(String email) {
        redisTemplate.delete("otp:" + email);
    }
}
