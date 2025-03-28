package com.watermelon.sso.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.watermelon.sso.entity.UserRoleAssociation;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户角色关联数据访问层
 */
@Mapper
public interface UserRoleAssociationMapper extends BaseMapper<UserRoleAssociation> {
    // 可以在此添加自定义的SQL方法
} 