package com.watermelon.sso.web;

import com.watermelon.sso.common.Result;
import com.watermelon.sso.entity.request.SendEmailCodeRequest;
import com.watermelon.sso.entity.request.UserLoginRequest;
import com.watermelon.sso.entity.request.UserRegisterRequest;
import com.watermelon.sso.entity.response.LoginResponse;
import com.watermelon.sso.entity.response.TokenResponse;
import com.watermelon.sso.manager.EmailManager;
import com.watermelon.sso.service.AuthService;
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
    private EmailManager emailManager;

    @Autowired
    private AuthService authService;


    /**
     * 发送邮箱验证码
     */
    @PostMapping("/email/code/send")
    public Result<Void> sendEmailCode(@RequestBody @Valid SendEmailCodeRequest request) {
        boolean success = emailManager.sendVerificationCode(request.getEmail());
        if (success) {
            return Result.success();
        } else {
            return Result.error(16000, "Email sending limit exceeded");
        }
    }

    /**
     * 用户邮箱注册
     */
    @PostMapping("/register")
    public Result<LoginResponse> register(@RequestBody @Valid UserRegisterRequest request) {
        LoginResponse response = authService.register(request);
        return Result.success(response);
    }

    /**
     * 用户邮箱登录
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody @Valid UserLoginRequest request) {
        LoginResponse response = authService.login(request);
        return Result.success(response);
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public Result<Boolean> logout(@RequestHeader(value = "Access-Token", required = false) String token) {
        boolean success = authService.logout(token);
        return Result.success(success);
    }

    /**
     * 刷新令牌
     */
    @PostMapping("/token/refresh")
    public Result<TokenResponse> refreshToken(@RequestHeader(value = "Access-Token", required = false) String token) {
        TokenResponse response = authService.refreshToken(token);
        return Result.success(response);
    }
} 