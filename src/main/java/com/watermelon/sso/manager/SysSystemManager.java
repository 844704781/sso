package com.watermelon.sso.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.watermelon.sso.entity.SysSystem;
import com.watermelon.sso.mapper.SysSystemMapper;
import org.springframework.stereotype.Component;

/**
 * 系统管理层
 */
@Component
public class SysSystemManager extends ServiceImpl<SysSystemMapper, SysSystem> {
    // 可以添加特定业务方法
} 