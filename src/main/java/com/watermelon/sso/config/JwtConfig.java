package com.watermelon.sso.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT配置类，用于生成和解析JWT令牌
 */
@Configuration
public class JwtConfig {

    // 签名密钥，在应用启动时随机生成
    private final SecretKey secretKey;
    
    // 随机生成的密钥长度（字节）
    private static final int SECRET_KEY_LENGTH = 64;
    
    // 构造函数，生成随机密钥
    public JwtConfig() {
        // 生成随机密钥
        byte[] keyBytes = generateRandomKey();
        secretKey = Keys.hmacShaKeyFor(keyBytes);
        
        // 打印密钥信息供参考（在生产环境可以移除）
        String encodedKey = Base64.getEncoder().encodeToString(keyBytes);
        System.out.println("Generated JWT secret key: " + encodedKey);
    }
    
    /**
     * 生成随机密钥
     * 
     * @return 随机密钥字节数组
     */
    private byte[] generateRandomKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[SECRET_KEY_LENGTH];
        secureRandom.nextBytes(key);
        return key;
    }

    /**
     * 生成JWT令牌
     *
     * @param claims JWT载荷中包含的信息
     * @param subject 主题（通常是用户ID或用户名）
     * @param expirationTime 过期时间
     * @return JWT令牌
     */
    public String generateToken(Map<String, Object> claims, String subject, long expirationTime) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(expirationTime))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 从JWT令牌中提取载荷信息
     *
     * @param token JWT令牌
     * @return 载荷信息
     */
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 获取JWT令牌中的特定载荷信息
     *
     * @param token JWT令牌
     * @param claimsResolver 载荷解析函数
     * @param <T> 返回类型
     * @return 载荷信息
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 从JWT令牌中提取主题（通常是用户ID或用户名）
     *
     * @param token JWT令牌
     * @return 主题
     */
    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * 从JWT令牌中提取过期时间
     *
     * @param token JWT令牌
     * @return 过期时间
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * 检查JWT令牌是否有效
     * 
     * @param token JWT令牌
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
} 