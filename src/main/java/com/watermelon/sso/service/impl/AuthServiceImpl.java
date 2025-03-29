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
import com.watermelon.sso.manager.TokenServiceManager;
import com.watermelon.sso.manager.UserManager;
import com.watermelon.sso.service.AuthService;
import com.watermelon.sso.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
    
    @Autowired
    private TokenServiceManager tokenServiceManager;
    
    // 从配置文件读取内部token有效期（单位：小时）
    @Value("${app.token.internal-expire:30}")
    private int internalTokenExpireDays;
    
    // 从配置文件读取访问token有效期（单位：小时）
    @Value("${app.token.access-expire:24}")
    private int accessTokenExpireHours;
    
    // 计算默认token有效期（单位：毫秒）
    private long getDefaultTokenExpiration() {
        return System.currentTimeMillis() + internalTokenExpireDays * 24 * 60 * 60 * 1000L;
    }
    
    // 计算访问token有效期（单位：毫秒）
    private long getAccessTokenExpiration() {
        return System.currentTimeMillis() + accessTokenExpireHours * 60 * 60 * 1000L;
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
        user.setUuid("u" + UUID.randomUUID().toString().replace("-", "").substring(0, 10));
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setSuspend(0); // 默认不暂停
        user.setAdmin(0);   // 默认普通用户
        
        // 内部系统调用的token
        String internalToken = UUID.randomUUID().toString().replace("-", "");
        long dbExpiration = getDefaultTokenExpiration();
        user.setToken(internalToken);
        user.setExpiration(dbExpiration);
        
        // 保存用户
        boolean success = userManager.save(user);
        if (!success) {
            throw new ServiceException(ResultCode.USER_CREATE_FAILED, "Failed to create user");
        }
        
        // 计算访问令牌过期时间
        long accessTokenExpiration = getAccessTokenExpiration();

        // 创建令牌载荷
        Map<String, Object> claims = new HashMap<>();
        claims.put("uuid", user.getUuid());
        claims.put("email", user.getEmail());
        claims.put("role", user.getAdmin() == 1 ? "admin" : "user");

        // 生成访问令牌并存储到Redis
        String accessToken = tokenService.generateAndStoreToken(user.getUuid(), claims, accessTokenExpiration);
        
        // 构建响应
        LoginResponse response = new LoginResponse();
        response.setUuid(user.getUuid());
        response.setNick_name(user.getNickName());
        response.setEmail(user.getEmail());
        response.setAccess_token(accessToken);
        response.setExpiration(accessTokenExpiration);
        response.setAvatar_url(user.getAvatarUrl());
        
        log.info("New user registered: {}", user.getEmail());
        return response;
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
        
        // 计算访问令牌过期时间
        long accessTokenExpiration = getAccessTokenExpiration();

        // 创建令牌载荷
        Map<String, Object> claims = new HashMap<>();
        claims.put("uuid", user.getUuid());
        claims.put("email", user.getEmail());
        claims.put("role", user.getAdmin() == 1 ? "admin" : "user");

        // 生成访问令牌并存储到Redis
        String accessToken = tokenService.generateAndStoreToken(user.getUuid(), claims, accessTokenExpiration);
        
        // 内部系统调用的token仍然保持不变
        String internalToken = UUID.randomUUID().toString().replace("-", "");
        long dbExpiration = getDefaultTokenExpiration();
        
        // 更新用户token和最后登录时间（仅更新数据库中存储的token，而非access_token）
        user.setToken(internalToken);
        user.setExpiration(dbExpiration);
        user.setLastOnlineTime(System.currentTimeMillis());
        userManager.updateById(user);
        
        // 构建响应
        LoginResponse response = new LoginResponse();
        response.setUuid(user.getUuid());
        response.setNick_name(user.getNickName());
        response.setEmail(user.getEmail());
        response.setAccess_token(accessToken);
        response.setExpiration(accessTokenExpiration);
        response.setAvatar_url(user.getAvatarUrl());
        
        log.info("User logged in: {}", user.getEmail());
        return response;
    }
    
    @Override
    public boolean logout(String token) {
        if (token == null) {
            return false;
        }
        
        // 从Redis中删除令牌
        return tokenService.removeToken(token);
    }
    
    @Override
    public TokenResponse refreshToken(String token) {
        if (token == null) {
            throw new ServiceException(ResultCode.TOKEN_INVALID, "Invalid token");
        }
        
        // 验证令牌并获取用户UUID
        String uuid = tokenService.validateToken(token);
        if (uuid == null) {
            throw new ServiceException(ResultCode.TOKEN_INVALID, "Invalid token");
        }
        
        // 查询用户
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getUuid, uuid);
        User user = userManager.getOne(wrapper);
        
        if (user == null) {
            throw new ServiceException(ResultCode.NO_ITEM_USER, "User does not exist");
        }
        
        // 计算新的访问令牌过期时间
        long accessTokenExpiration = getAccessTokenExpiration();
        
        // 刷新令牌
        String newAccessToken = tokenService.refreshToken(token, accessTokenExpiration);
        if (newAccessToken == null) {
            throw new ServiceException(ResultCode.TOKEN_INVALID, "Failed to refresh token");
        }
        
        // 更新用户最后在线时间
        user.setLastOnlineTime(System.currentTimeMillis());
        userManager.updateById(user);
        
        // 构建响应
        TokenResponse response = new TokenResponse();
        response.setAccess_token(newAccessToken);
        response.setExpiration(accessTokenExpiration);
        
        log.info("Token refreshed for user: {}", user.getEmail());
        return response;
    }
} 