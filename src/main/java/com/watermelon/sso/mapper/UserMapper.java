package com.watermelon.sso.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.watermelon.sso.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户数据访问层
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 可以在此添加自定义的SQL方法
} 