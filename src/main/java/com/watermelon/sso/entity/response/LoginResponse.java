package com.watermelon.sso.entity.response;

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
    private String nick_name;
    
    /**
     * 邮箱地址
     */
    private String email;
    
    /**
     * 令牌（前端访问使用）
     */
    private String access_token;
    
    /**
     * 过期时间（毫秒时间戳）
     */
    private Long expiration;
    
    /**
     * 头像URL
     */
    private String avatar_url;
} 