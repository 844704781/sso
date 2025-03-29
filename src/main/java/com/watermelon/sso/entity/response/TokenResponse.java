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
    private String access_token;

    /**
     * 过期时间（毫秒时间戳）
     */
    private Long expiration;
} 