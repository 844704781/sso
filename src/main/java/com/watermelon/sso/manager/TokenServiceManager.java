package com.watermelon.sso.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.watermelon.sso.config.JwtConfig;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Token服务实现类，使用JWT作为token格式，并使用Redis存储和管理访问令牌
 */
@Slf4j
@Service
public class TokenServiceManager {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtConfig jwtConfig;

    // Redis中存储用户令牌映射的前缀
    private static final String USER_TOKEN_PREFIX = "sso:user:token:";

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

    public String refreshToken(String oldToken, long expiration) {
        // 验证JWT签名
        if (!jwtConfig.validateToken(oldToken)) {
            log.warn("Invalid JWT signature for token refresh");
            return null;
        }

        String uuid = validateToken(oldToken);
        if (uuid == null) {
            log.warn("Cannot refresh invalid token: {}", oldToken);
            return null;
        }

        try {
            // 从JWT中提取subject和claims
            String subject = jwtConfig.extractSubject(oldToken);

            // 获取旧token在Redis中的信息
            String tokenKey = USER_TOKEN_PREFIX + oldToken;
            Object tokenInfoObj = redisTemplate.opsForValue().get(tokenKey);
            Map<String, Object> tokenInfo = objectMapper.readValue(tokenInfoObj.toString(), HashMap.class);

            // 提取claims
            Map<String, Object> claims = (Map<String, Object>) tokenInfo.get("claims");

            // 删除旧token
            redisTemplate.delete(tokenKey);

            // 生成新token
            return generateAndStoreToken(uuid, claims, expiration);
        } catch (Exception e) {
            log.error("Failed to refresh token: {}", e.getMessage());
            return null;
        }
    }

    public String getUuidFromToken(String token) {
        if (token == null) {
            return null;
        }

        // 验证JWT签名
        if (!jwtConfig.validateToken(token)) {
            log.warn("Invalid JWT signature");
            return null;
        }

        // 尝试从JWT的subject中获取uuid
        try {
            return jwtConfig.extractSubject(token);
        } catch (Exception e) {
            log.error("Failed to extract subject from JWT: {}", e.getMessage());
            return null;
        }
    }

    public Long getTokenExpiration(String token) {
        if (token == null) {
            return null;
        }

        // 验证JWT签名
        if (!jwtConfig.validateToken(token)) {
            log.warn("Invalid JWT signature");
            return null;
        }

        String tokenKey = USER_TOKEN_PREFIX + token;
        Object tokenInfoObj = redisTemplate.opsForValue().get(tokenKey);

        if (tokenInfoObj == null) {
            log.warn("Token not found: {}", token);
            return null;
        }

        try {
            // 解析token信息
            Map<String, Object> tokenInfo = objectMapper.readValue(tokenInfoObj.toString(), HashMap.class);
            return ((Number) tokenInfo.get("expiration")).longValue();
        } catch (Exception e) {
            log.error("Failed to get token expiration: {}", e.getMessage());
            return null;
        }
    }
} 