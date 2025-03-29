package com.watermelon.sso.entity.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 令牌响应
 */
@Data
public class ApiTokenResponse {

    /**
     * 令牌（前端访问使用）
     */
    @JsonProperty("api_token")
    private String apiToken;

    /**
     * 过期时间（毫秒时间戳）
     */
    private Long expiration;
} 