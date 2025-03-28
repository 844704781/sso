package com.watermelon.sso.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.watermelon.sso.entity.RolePermissionAssociation;
import com.watermelon.sso.mapper.RolePermissionAssociationMapper;
import org.springframework.stereotype.Component;

/**
 * 角色权限关联管理层
 */
@Component
public class RolePermissionAssociationManager extends ServiceImpl<RolePermissionAssociationMapper, RolePermissionAssociation> {
    // 可以添加特定业务方法
} 