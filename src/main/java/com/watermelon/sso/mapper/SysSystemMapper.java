package com.watermelon.sso.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.watermelon.sso.entity.SysSystem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统数据访问层
 */
@Mapper
public interface SysSystemMapper extends BaseMapper<SysSystem> {
    // 可以在此添加自定义的SQL方法
} 