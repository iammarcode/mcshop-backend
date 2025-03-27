package com.marco.mcshop.auth.service;

import java.util.List;

public interface RedisService<String, Object> {

    void set(String key, Object value, long time);


    void set(String key, Object value);

    Object get(String key);

    Boolean del(String key);

    Long del(List<String> keys);

    Boolean expire(String key, long time);

    Long getExpire(String key);
}