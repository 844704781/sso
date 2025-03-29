package com.watermelon.sso.entity.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 邮箱验证码请求
 */
@Data
public class EmailCodeRequest {

    /**
     * 邮箱地址
     */
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    @JsonProperty("email")
    private String email;
} 