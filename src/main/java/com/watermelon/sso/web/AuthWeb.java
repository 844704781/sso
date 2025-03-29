package com.watermelon.sso.web;

import com.watermelon.sso.common.Result;
import com.watermelon.sso.common.ResultCode;
import com.watermelon.sso.entity.request.EmailCodeRequest;
import com.watermelon.sso.entity.request.VerifyEmailCodeRequest;
import com.watermelon.sso.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 认证接口
 */
@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthWeb {

    @Autowired
    private EmailService emailService;

    /**
     * 发送邮箱验证码
     */
    @PostMapping("/email/code")
    public Result<Boolean> sendEmailCode(@RequestBody @Valid EmailCodeRequest request) {
        boolean success = emailService.sendVerificationCode(request.getEmail());

        if (!success) {
            return Result.error(ResultCode.EMAIL_SEND_LIMIT_EXCEEDED, "Email verification code sending limit exceeded (20 per 6 hours)");
        }

        return Result.success(true);
    }

    /**
     * 验证邮箱验证码
     */
    @PostMapping("/email/verify")
    public Result<Boolean> verifyEmailCode(@RequestBody @Valid VerifyEmailCodeRequest request) {
        boolean success = emailService.verifyEmailCode(request.getEmail(), request.getCode());

        if (!success) {
            return Result.error(ResultCode.EMAIL_CODE_INVALID, "Invalid verification code");
        }

        return Result.success(true);
    }
} 