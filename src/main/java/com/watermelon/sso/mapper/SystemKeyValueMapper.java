package com.watermelon.sso.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.watermelon.sso.entity.SystemKeyValue;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统配置数据访问层
 */
@Mapper
public interface SystemKeyValueMapper extends BaseMapper<SystemKeyValue> {
    // 可以在此添加自定义的SQL方法
} 