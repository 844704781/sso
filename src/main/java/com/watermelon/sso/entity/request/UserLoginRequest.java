package com.watermelon.sso.entity.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 用户邮箱登录请求
 */
@Data
public class UserLoginRequest {

    /**
     * 邮箱地址
     */
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    @JsonProperty("email")
    private String email;

    /**
     * 密码
     */
    @NotBlank(message = "Password cannot be empty")
    @JsonProperty("password")
    private String password;
} 