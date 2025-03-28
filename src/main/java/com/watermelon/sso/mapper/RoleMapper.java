package com.watermelon.sso.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.watermelon.sso.entity.Role;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色数据访问层
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    // 可以在此添加自定义的SQL方法
} 