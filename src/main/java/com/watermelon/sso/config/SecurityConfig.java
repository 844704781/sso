package com.watermelon.sso.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;

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
            .csrf(csrf -> csrf.disable())
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/sso/auth/**").permitAll()
                // 添加其他公共端点
                .anyRequest().permitAll()
            );
            // 如需要，添加您的自定义JWT过滤器
            
        return http.build();
    }
} 