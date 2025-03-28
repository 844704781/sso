package com.watermelon.sso.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.watermelon.sso.entity.SystemKeyValue;
import com.watermelon.sso.mapper.SystemKeyValueMapper;
import org.springframework.stereotype.Component;

/**
 * 系统配置管理层
 */
@Component
public class SystemKeyValueManager extends ServiceImpl<SystemKeyValueMapper, SystemKeyValue> {
    // 可以添加特定业务方法
} 