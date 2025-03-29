package com.watermelon.sso.entity.response;

import lombok.Data;

/**
 * 令牌响应
 */
@Data
public class TokenResponse {

    /**
     * 令牌（前端访问使用）
     */
    private String accessToken;

    /**
     * 刷新令牌（用于刷新访问令牌）
     */
    private String refreshToken;

    /**
     * 访问令牌过期时间（毫秒时间戳）
     */
    private long accessTokenExpiration;

    /**
     * 刷新令牌过期时间（毫秒时间戳）
     */
    private long refreshTokenExpiration;
} 