package com.watermelon.sso.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.watermelon.sso.common.ResultCode;
import com.watermelon.sso.common.ServiceException;
import com.watermelon.sso.entity.User;
import com.watermelon.sso.entity.request.UserLoginRequest;
import com.watermelon.sso.entity.request.UserRegisterRequest;
import com.watermelon.sso.entity.response.LoginResponse;
import com.watermelon.sso.entity.response.TokenResponse;
import com.watermelon.sso.manager.AccessTokenManager;
import com.watermelon.sso.manager.UserManager;
import com.watermelon.sso.service.AuthService;
import com.watermelon.sso.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证服务实现
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserManager userManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 从配置文件读取访问token有效期（单位：小时）
    @Value("${app.token.access-expire:2}")
    private int accessTokenExpireHours;

    // 添加refresh token过期时间配置（7天）
    @Value("${app.token.refresh-expire:168}")
    private int refreshTokenExpireHours;

    @Autowired
    private AccessTokenManager tokenManager;

    /**
     * 构建响应体
     */
    private static LoginResponse buildLoginResponse(
            User user,
            String accessToken,
            String refreshToken,
            long accessTokenExpiration,
            long refreshTokenExpiration
    ) {
        LoginResponse response = new LoginResponse();
        response.setUuid(user.getUuid());
        response.setNickName(user.getNickName());
        response.setEmail(user.getEmail());
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setAccessTokenExpiration(accessTokenExpiration);
        response.setRefreshTokenExpiration(refreshTokenExpiration);
        response.setAvatarUrl(user.getAvatarUrl());
        return response;
    }

    // 计算访问token有效期（单位：毫秒）
    private long getAccessTokenExpiration() {
        return System.currentTimeMillis() + accessTokenExpireHours * 60 * 60 * 1000L;
    }

    // 获取refresh token过期时间
    private long getRefreshTokenExpiration() {
        return System.currentTimeMillis() + refreshTokenExpireHours * 60 * 60 * 1000L;
    }

    @Override
    @Transactional
    public LoginResponse register(UserRegisterRequest request) {
        // 验证邮箱验证码
        boolean valid = emailService.verifyEmailCode(request.getEmail(), request.getVerificationCode());
        if (!valid) {
            throw new ServiceException(ResultCode.EMAIL_CODE_INVALID, "Invalid verification code");
        }

        // 检查邮箱是否已存在
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getEmail, request.getEmail());
        if (userManager.count(wrapper) > 0) {
            throw new ServiceException(ResultCode.EXIST_USER_EMAIL, "Email already exists");
        }

        // 创建用户
        User user = new User();
        user.setUuid(RandomStringUtils.randomAlphanumeric(32));
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setSuspend(1); // 默认不暂停
        user.setAdmin(0);   // 默认普通用户

        // 保存用户
        boolean success = userManager.save(user);
        if (!success) {
            throw new ServiceException(ResultCode.USER_CREATE_FAILED, "Failed to create user");
        }

        return buildLoginResponse(user);
    }

    private LoginResponse buildLoginResponse(User user) {
        // 计算两个token的过期时间
        long accessTokenExpiration = getAccessTokenExpiration();
        long refreshTokenExpiration = getRefreshTokenExpiration();

        // 创建令牌载荷
        Map<String, Object> accessTokenClaims = new HashMap<>();
        accessTokenClaims.put("uuid", user.getUuid());
        accessTokenClaims.put("email", user.getEmail());
        accessTokenClaims.put("type", "access_token");

        Map<String, Object> refreshTokenClaims = new HashMap<>();
        refreshTokenClaims.put("uuid", user.getUuid());
        refreshTokenClaims.put("email", user.getEmail());
        refreshTokenClaims.put("type", "refresh_token");

        // 生成访问令牌和刷新令牌
        String accessToken = tokenManager.generateAndStoreToken(
                user.getUuid() + ":access",
                accessTokenClaims,
                accessTokenExpiration
        );

        String refreshToken = tokenManager.generateAndStoreToken(
                user.getUuid() + ":refresh",
                refreshTokenClaims,
                refreshTokenExpiration
        );

        log.info("New user registered: {}", user.getEmail());

        // 构建响应
        return buildLoginResponse(user, accessToken, refreshToken, accessTokenExpiration, refreshTokenExpiration);
    }

    @Override
    public LoginResponse login(UserLoginRequest request) {
        // 查询用户
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getEmail, request.getEmail());
        User user = userManager.getOne(wrapper);

        // 用户不存在
        if (user == null) {
            throw new ServiceException(ResultCode.NO_ITEM_USER, "User does not exist");
        }

        // 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ServiceException(ResultCode.PASSWORD_INCORRECT, "Incorrect password");
        }

        // 用户被暂停
        if (user.getSuspend() != null && user.getSuspend() == 1) {
            throw new ServiceException(ResultCode.AUTH_FAILED, "User account is suspended");
        }

        return buildLoginResponse(user);
    }

    @Override
    public boolean logout(String token) {
        if (token == null) {
            return false;
        }
        // 从Redis中删除令牌
        return tokenManager.removeToken(token);
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        if (refreshToken == null) {
            throw new ServiceException(ResultCode.TOKEN_INVALID, "Invalid refresh token");
        }

        // 验证刷新令牌并获取用户UUID（需要验证token类型是refresh_token）
        Map<String, Object> claims = tokenManager.validateAndGetClaims(refreshToken);
        if (claims == null || !"refresh_token".equals(claims.get("type"))) {
            throw new ServiceException(ResultCode.TOKEN_INVALID, "Invalid refresh token type");
        }

        String uuid = (String) claims.get("uuid");
        if (uuid == null) {
            throw new ServiceException(ResultCode.TOKEN_INVALID, "Invalid refresh token");
        }

        // 查询用户
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getUuid, uuid);
        User user = userManager.getOne(wrapper);

        if (user == null) {
            throw new ServiceException(ResultCode.NO_ITEM_USER, "User does not exist");
        }

        // 用户被暂停
        if (user.getSuspend() != null && user.getSuspend() == 1) {
            throw new ServiceException(ResultCode.AUTH_FAILED, "User account is suspended");
        }

        // 计算新的token过期时间
        long accessTokenExpiration = getAccessTokenExpiration();
        long refreshTokenExpiration = getRefreshTokenExpiration();

        // 创建新的访问令牌载荷
        Map<String, Object> accessTokenClaims = new HashMap<>();
        accessTokenClaims.put("uuid", user.getUuid());
        accessTokenClaims.put("email", user.getEmail());
        accessTokenClaims.put("type", "access_token");

        // 创建新的刷新令牌载荷
        Map<String, Object> refreshTokenClaims = new HashMap<>();
        refreshTokenClaims.put("uuid", user.getUuid());
        refreshTokenClaims.put("email", user.getEmail());
        refreshTokenClaims.put("type", "refresh_token");

        // 生成新的访问令牌和刷新令牌
        String newAccessToken = tokenManager.generateAndStoreToken(
                user.getUuid() + ":access",
                accessTokenClaims,
                accessTokenExpiration
        );

        String newRefreshToken = tokenManager.generateAndStoreToken(
                user.getUuid() + ":refresh",
                refreshTokenClaims,
                refreshTokenExpiration
        );

        // 删除旧的刷新令牌
        tokenManager.removeToken(refreshToken);

        // 更新用户最后在线时间
        user.setLastOnlineTime(System.currentTimeMillis());
        userManager.updateById(user);

        // 构建响应
        TokenResponse response = new TokenResponse();
        response.setAccessToken(newAccessToken);
        response.setRefreshToken(newRefreshToken);
        response.setAccessTokenExpiration(accessTokenExpiration);
        response.setRefreshTokenExpiration(refreshTokenExpiration);

        log.info("Tokens refreshed for user: {}", user.getEmail());
        return response;
    }
} 