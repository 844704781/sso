package com.watermelon.sso.entity.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 登录响应
 */
@Data
public class LoginResponse {

    /**
     * 用户编号
     */
    private String uuid;

    /**
     * 用户昵称
     */
    @JsonProperty("nick_name")
    private String nickName;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 令牌（前端访问使用）
     */
    @JsonProperty("access_token")
    private String accessToken;

    /**
     * 过期时间（毫秒时间戳）
     */
    @JsonProperty("access_token_expiration")
    private long accessTokenExpiration;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("refresh_token_expiration")
    private long refreshTokenExpiration;

    /**
     * 头像URL
     */
    @JsonProperty("avatar_url")
    private String avatarUrl;
} 