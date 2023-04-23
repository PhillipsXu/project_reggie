package com.pf.utils;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component("tokenUtil")
public class TokenUtil {

    @CreateCache(area = "token", name = "jetCache_", expire = 30, timeUnit = TimeUnit.DAYS)
    private Cache<String, String> cache;

    public String generateToken(String username) {
        String token = UUID.randomUUID().toString();
        try {
            cache.put(username, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    public boolean verifyToken(String username, String token) {
        String cacheToken;
        try {
            cacheToken = cache.get(username);
            log.info("cacheToken     : {}", cacheToken);
            if (Objects.equals(token, cacheToken)) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean removeToken(String username) {
        try {
            return cache.remove(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
