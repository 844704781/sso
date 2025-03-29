package com.watermelon.sso.entity.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 用户密码请求
 */
@Data
public class UserPasswordRequest {

    /**
     * 新密码
     */
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, max = 32, message = "Password length must be between 8 and 32 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$", message = "Password must contain at least one lowercase letter, one uppercase letter, and one digit")
    @JsonProperty("password")
    private String password;
} 