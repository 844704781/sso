package com.watermelon.sso.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.watermelon.sso.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Token服务实现类，使用JWT作为token格式，并使用Redis存储和管理访问令牌
 */
@Slf4j
@Service
public class AccessTokenManager {

    // Redis中存储用户令牌映射的前缀
    private static final String USER_TOKEN_PREFIX = "sso:user:token:";
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtConfig jwtConfig;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public String generateAndStoreToken(String uuid, Map<String, Object> claims, long expiration) {
        try {
            // 生成JWT令牌
            String jwtToken = jwtConfig.generateToken(claims, uuid, expiration);

            // 计算过期时间（转换为相对时间，单位：毫秒）
            long ttlMillis = expiration - System.currentTimeMillis();
            if (ttlMillis <= 0) {
                log.warn("Token expiration time is already passed: {}", expiration);
                ttlMillis = 3600000; // 默认设置1小时
            }

            // 为每个用户存储最近的令牌（可用于令牌刷新和管理）
            String userTokenKey = USER_TOKEN_PREFIX + uuid;
            redisTemplate.opsForValue().set(userTokenKey, jwtToken, ttlMillis, TimeUnit.MILLISECONDS);

            log.info("Generated and stored JWT token for user: {}", uuid);
            return jwtToken;
        } catch (Exception e) {
            log.error("Failed to serialize token info: {}", e.getMessage());
            return null;
        }
    }

    public String validateToken(String token) {
        if (token == null) {
            return null;
        }

        // 首先验证JWT签名
        if (!jwtConfig.validateToken(token)) {
            log.warn("Invalid JWT signature for token");
            return null;
        }

        // 从token中提取uuid返回
        try {
            // 使用JWT配置对象从token中提取subject，subject应该是用户的uuid
            String uuid = jwtConfig.extractSubject(token);

            // 检查Redis中是否存在该token（是否被撤销）
            String tokenKey = USER_TOKEN_PREFIX + uuid;
            Object tokenInfoObj = redisTemplate.opsForValue().get(tokenKey);

            if (tokenInfoObj == null) {
                log.warn("Token not found in Redis: {}", token);
                return null;
            }

            // 验证token中的uuid与Redis中存储的uuid是否一致
            Map<String, Object> tokenInfo = objectMapper.readValue(tokenInfoObj.toString(), HashMap.class);
            String storedUuid = (String) tokenInfo.get("uuid");

            if (!uuid.equals(storedUuid)) {
                log.warn("Token UUID mismatch: {} vs {}", uuid, storedUuid);
                return null;
            }

            return uuid;
        } catch (Exception e) {
            log.error("Failed to extract or validate UUID from token: {}", e.getMessage());
            return null;
        }
    }

    public boolean removeToken(String token) {
        if (token == null) {
            return false;
        }

        // 验证JWT签名
        if (!jwtConfig.validateToken(token)) {
            log.warn("Invalid JWT signature for token removal");
            return false;
        }

        String tokenKey = USER_TOKEN_PREFIX + validateToken(token);
        Object tokenInfoObj = redisTemplate.opsForValue().get(tokenKey);

        if (tokenInfoObj == null) {
            log.warn("Token not found in Redis for removal: {}", token);
            return false;
        }

        try {
            // 删除用户令牌映射
            redisTemplate.delete(tokenKey);

            log.info("Removed token for user: {}", tokenKey);
            return true;
        } catch (Exception e) {
            log.error("Failed to remove token: {}", e.getMessage());
            return false;
        }
    }


    /**
     * 验证token并获取claims信息
     *
     * @param token 需要验证的token
     * @return claims信息，如果token无效则返回null
     */
    public Map<String, Object> validateAndGetClaims(String token) {
        try {
            // 从Redis中获取token信息
            String storedToken = (String) redisTemplate.opsForValue().get(token);
            if (storedToken == null) {
                log.warn("Token not found in Redis: {}", token);
                return null;
            }

            // 解析JWT token
            Claims claims = jwtConfig.extractAllClaims(token);

            // 验证token是否过期
            Date expiration = claims.getExpiration();
            if (expiration != null && expiration.before(new Date())) {
                log.warn("Token has expired: {}", token);
                // 删除过期的token
                redisTemplate.delete(token);
                return null;
            }

            // 将Claims转换为Map
            Map<String, Object> claimsMap = new HashMap<>(claims);

            return claimsMap;
        } catch (JwtException e) {
            log.error("Failed to validate token: {}", token, e);
            return null;
        } catch (Exception e) {
            log.error("Unexpected error while validating token: {}", token, e);
            return null;
        }
    }
}