package com.watermelon.sso.web;

import com.watermelon.sso.common.Result;
import com.watermelon.sso.common.ServiceException;
import com.watermelon.sso.entity.request.SendEmailCodeRequest;
import com.watermelon.sso.entity.request.UserLoginRequest;
import com.watermelon.sso.entity.request.UserRegisterRequest;
import com.watermelon.sso.entity.request.VerifyEmailCodeRequest;
import com.watermelon.sso.entity.response.LoginResponse;
import com.watermelon.sso.entity.response.TokenResponse;
import com.watermelon.sso.service.AuthService;
import com.watermelon.sso.service.EmailService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 认证相关接口
 */
@Slf4j
@RestController
@RequestMapping("/sso/auth")
@Validated
public class AuthWeb {

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private TokenService tokenService;

    /**
     * 发送邮箱验证码
     */
    @PostMapping("/email/code/send")
    public Result<Void> sendEmailCode(@RequestBody @Valid SendEmailCodeRequest request) {
        try {
            boolean success = emailService.sendVerificationCode(request.getEmail());
            if (success) {
                return Result.success();
            } else {
                return Result.error(16000, "Email sending limit exceeded");
            }
        } catch (Exception e) {
            log.error("Failed to send email verification code: {}", e.getMessage());
            return Result.error(500, "Failed to send email verification code");
        }
    }

    /**
     * 验证邮箱验证码
     */
    @PostMapping("/email/code/verify")
    public Result<Boolean> verifyEmailCode(@RequestBody @Valid VerifyEmailCodeRequest request) {
        try {
            boolean valid = emailService.verifyEmailCode(request.getEmail(), request.getCode());
            return Result.success(valid);
        } catch (Exception e) {
            log.error("Failed to verify email code: {}", e.getMessage());
            return Result.error(500, "Failed to verify email code");
        }
    }
    
    /**
     * 用户邮箱注册
     */
    @PostMapping("/register")
    public Result<LoginResponse> register(@RequestBody @Valid UserRegisterRequest request) {
        try {
            LoginResponse response = authService.register(request);
            return Result.success(response);
        } catch (ServiceException e) {
            log.error("Registration failed: {}", e.getMessage());
            return Result.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("Registration failed with unexpected error: {}", e.getMessage());
            return Result.error(500, "Registration failed");
        }
    }
    
    /**
     * 用户邮箱登录
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody @Valid UserLoginRequest request) {
        try {
            LoginResponse response = authService.login(request);
            return Result.success(response);
        } catch (ServiceException e) {
            log.error("Login failed: {}", e.getMessage());
            return Result.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("Login failed with unexpected error: {}", e.getMessage());
            return Result.error(500, "Login failed");
        }
    }
    
    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public Result<Boolean> logout(@RequestHeader(value = "Access-Token", required = false) String token) {
        try {
            boolean success = authService.logout(token);
            return Result.success(success);
        } catch (Exception e) {
            log.error("Logout failed: {}", e.getMessage());
            return Result.error(500, "Logout failed");
        }
    }
    
    /**
     * 刷新令牌
     */
    @PostMapping("/token/refresh")
    public Result<TokenResponse> refreshToken(@RequestHeader(value = "Access-Token", required = false) String token) {
        try {
            TokenResponse response = authService.refreshToken(token);
            return Result.success(response);
        } catch (ServiceException e) {
            log.error("Token refresh failed: {}", e.getMessage());
            return Result.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("Token refresh failed with unexpected error: {}", e.getMessage());
            return Result.error(500, "Token refresh failed");
        }
    }
    
    /**
     * 验证令牌有效性
     */
    @GetMapping("/token/validate")
    public Result<Boolean> validateToken(@RequestHeader(value = "Access-Token", required = false) String token) {
        try {
            if (token == null) {
                return Result.success(false);
            }
            
            String uuid = tokenService.validateToken(token);
            return Result.success(uuid != null);
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            return Result.error(500, "Token validation failed");
        }
    }
} 