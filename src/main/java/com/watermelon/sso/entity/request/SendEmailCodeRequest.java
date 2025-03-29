package com.watermelon.sso.entity.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * 发送邮箱验证码请求
 */
@Data
public class SendEmailCodeRequest {

    /**
     * 邮箱
     */
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    @JsonProperty("email")
    private String email;
} 