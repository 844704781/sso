package com.watermelon.sso.service;

import com.watermelon.sso.entity.request.UserLoginRequest;
import com.watermelon.sso.entity.request.UserRegisterRequest;
import com.watermelon.sso.entity.response.LoginResponse;
import com.watermelon.sso.entity.response.TokenResponse;

/**
 * 认证服务接口
 */
public interface AuthService {

    /**
     * 用户邮箱注册
     *
     * @param request 注册请求
     * @return 注册结果
     */
    LoginResponse register(UserRegisterRequest request);

    /**
     * 用户邮箱登录
     *
     * @param request 登录请求
     * @return 登录结果
     */
    LoginResponse login(UserLoginRequest request);

    /**
     * 退出登录
     *
     * @param token 用户令牌
     * @return 是否成功
     */
    boolean logout(String token);

    /**
     * 刷新令牌
     *
     * @param token 旧令牌
     * @return 新令牌
     */
    TokenResponse refreshToken(String token);
} 