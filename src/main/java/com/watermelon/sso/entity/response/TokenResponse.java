package com.watermelon.sso.entity.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Token响应
 */
@Data
public class TokenResponse {

    /**
     * 令牌
     */
    @JsonProperty("token")
    private String token;

    /**
     * 过期时间（毫秒时间戳）
     */
    @JsonProperty("expiration")
    private Long expiration;
} 