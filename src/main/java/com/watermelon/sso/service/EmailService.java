package com.watermelon.sso.service;

/**
 * 邮件服务接口
 */
public interface EmailService {

    /**
     * 发送验证码邮件
     *
     * @param email 接收邮箱
     * @return 是否发送成功
     */
    boolean sendVerificationCode(String email);

    /**
     * 验证邮箱验证码
     *
     * @param email 邮箱
     * @param code  验证码
     * @return 是否验证成功
     */
    boolean verifyEmailCode(String email, String code);
} 