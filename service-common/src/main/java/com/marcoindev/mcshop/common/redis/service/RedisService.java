package com.marcoindev.mcshop.common.redis.service;

import java.time.LocalDateTime;
import java.util.List;

public interface RedisService<String, Object> {

    void set(String key, Object value, long time);


    void set(String key, Object value);

    Object get(String key);

    Boolean del(String key);

    Long del(List<String> keys);

    Boolean expire(String key, long time);

    Long getExpire(String key);

    Long increment(java.lang.String key);

    Boolean expireAt(java.lang.String key, LocalDateTime expireTime);
}