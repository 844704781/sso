package com.watermelon.sso.entity.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 用户Token请求
 */
@Data
public class UserTokenRequest {

    /**
     * 过期时间（毫秒时间戳）
     */
    @NotNull(message = "Expiration time cannot be empty")
    @JsonProperty("expiration")
    private Long expiration;
} 