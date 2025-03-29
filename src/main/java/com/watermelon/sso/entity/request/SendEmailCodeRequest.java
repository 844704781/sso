package com.watermelon.sso.entity.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;




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