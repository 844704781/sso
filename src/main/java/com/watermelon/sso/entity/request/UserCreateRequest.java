package com.watermelon.sso.entity.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 创建用户请求
 */
@Data
public class UserCreateRequest {

    /**
     * 用户编号
     */
    @NotBlank(message = "UUID cannot be empty")
    @JsonProperty("uuid")
    private String uuid;

    /**
     * 用户昵称
     */
    @NotBlank(message = "Nickname cannot be empty")
    @JsonProperty("nick_name")
    private String nickName;

    /**
     * 手机号
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "Invalid phone number format")
    @JsonProperty("phone")
    private String phone;

    /**
     * 邮箱地址
     */
    @Email(message = "Invalid email format")
    @JsonProperty("email")
    private String email;

    /**
     * 密码
     */
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, max = 32, message = "Password length must be between 8 and 32 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$", message = "Password must contain at least one lowercase letter, one uppercase letter, and one digit")
    @JsonProperty("password")
    private String password;

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
     * 是否管理员（0-普通用户，1-管理员）
     */
    @JsonProperty("admin")
    private Integer admin;
} 