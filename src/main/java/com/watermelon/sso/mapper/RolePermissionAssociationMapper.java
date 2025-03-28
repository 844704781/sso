package com.watermelon.sso.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.watermelon.sso.entity.RolePermissionAssociation;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色权限关联数据访问层
 */
@Mapper
public interface RolePermissionAssociationMapper extends BaseMapper<RolePermissionAssociation> {
    // 可以在此添加自定义的SQL方法
} 