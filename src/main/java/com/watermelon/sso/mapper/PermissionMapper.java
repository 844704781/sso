package com.watermelon.sso.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.watermelon.sso.entity.Permission;
import org.apache.ibatis.annotations.Mapper;

/**
 * 权限数据访问层
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
    // 可以在此添加自定义的SQL方法
} 