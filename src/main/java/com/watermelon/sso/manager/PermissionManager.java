package com.watermelon.sso.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.watermelon.sso.entity.Permission;
import com.watermelon.sso.mapper.PermissionMapper;
import org.springframework.stereotype.Component;

/**
 * 权限管理层
 */
@Component
public class PermissionManager extends ServiceImpl<PermissionMapper, Permission> {
    // 可以添加特定业务方法
} 