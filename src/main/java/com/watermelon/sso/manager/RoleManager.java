package com.watermelon.sso.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.watermelon.sso.entity.Role;
import com.watermelon.sso.mapper.RoleMapper;
import org.springframework.stereotype.Component;

/**
 * 角色管理层
 */
@Component
public class RoleManager extends ServiceImpl<RoleMapper, Role> {
    // 可以添加特定业务方法
} 