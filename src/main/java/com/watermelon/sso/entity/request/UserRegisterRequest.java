package com.watermelon.sso.entity.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 用户邮箱注册请求
 */
@Data
public class UserRegisterRequest {

    /**
     * 邮箱地址
     */
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    @JsonProperty("email")
    private String email;

    /**
     * 验证码
     */
    @NotBlank(message = "Verification code cannot be empty")
    @JsonProperty("verification_code")
    private String verificationCode;

    /**
     * 密码
     */
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, max = 32, message = "Password length must be between 8 and 32 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$", message = "Password must contain at least one lowercase letter, one uppercase letter, and one digit")
    @JsonProperty("password")
    private String password;
} 