package com.watermelon.sso.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.watermelon.sso.entity.UserRoleAssociation;
import com.watermelon.sso.mapper.UserRoleAssociationMapper;
import org.springframework.stereotype.Component;

/**
 * 用户角色关联管理层
 */
@Component
public class UserRoleAssociationManager extends ServiceImpl<UserRoleAssociationMapper, UserRoleAssociation> {
    // 可以添加特定业务方法
} 