package com.watermelon.sso.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security配置类
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * 提供密码编码器Bean
     * BCryptPasswordEncoder自动为每个密码生成随机盐并存储
     * 加密结果格式: $2a$10$随机盐哈希结果
     * 其中:
     * - $2a$ 表示BCrypt算法版本
     * - 10 表示加密强度(cost factor)，决定散列函数执行次数，越高越安全但越慢
     * - 随机盐和哈希结果一起存储，无需单独保存盐值
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    /**
     * 配置安全过滤链
     * 禁用表单登录，使用自定义认证机制
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // 禁用CSRF保护，因为是API服务
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()  // 允许所有请求，认证由自定义逻辑处理
                )
                .formLogin(form -> form.disable())  // 禁用表单登录
                .httpBasic(basic -> basic.disable());  // 禁用HTTP Basic认证

        return http.build();
    }
} 