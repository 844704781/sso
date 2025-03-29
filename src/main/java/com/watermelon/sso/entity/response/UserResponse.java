package com.watermelon.sso.entity.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户响应
 */
@Data
public class UserResponse {

    /**
     * ID
     */
    @JsonProperty("id")
    private Long id;

    /**
     * 用户编号
     */
    @JsonProperty("uuid")
    private String uuid;

    /**
     * 用户昵称
     */
    @JsonProperty("nick_name")
    private String nickName;

    /**
     * 手机号
     */
    @JsonProperty("phone")
    private String phone;

    /**
     * 邮箱地址
     */
    @JsonProperty("email")
    private String email;

    /**
     * 是否暂停（0-正常，1-暂停）
     */
    @JsonProperty("suspend")
    private Integer suspend;

    /**
     * 是否管理员（0-普通用户，1-管理员）
     */
    @JsonProperty("admin")
    private Integer admin;

    /**
     * 头像URL
     */
    @JsonProperty("avatar_url")
    private String avatarUrl;

    /**
     * 手机国家代码
     */
    @JsonProperty("phone_country_code")
    private String phoneCountryCode;

    /**
     * 允许的服务
     */
    @JsonProperty("allow_service")
    private String allowService;

    /**
     * 最后在线时间
     */
    @JsonProperty("last_online_time")
    private LocalDateTime lastOnlineTime;

    /**
     * 创建时间
     */
    @JsonProperty("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonProperty("update_time")
    private LocalDateTime updateTime;
} 